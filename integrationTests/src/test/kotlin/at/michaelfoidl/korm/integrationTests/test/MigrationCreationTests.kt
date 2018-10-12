package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.migrations.MasterTable
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.integrationTests.database.TestDatabaseV1
import at.michaelfoidl.korm.integrationTests.database.TestDatabaseV2
import at.michaelfoidl.korm.integrationTests.testUtils.DatabaseConfigurationCreator
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.Migration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import test.EntityOneTable
import test.EntityTwoTable
import java.nio.file.Paths

class MigrationCreationTests {

    private val configurationCreator = DatabaseConfigurationCreator(
            databaseType = DatabaseType.SQLite,
            migrationPackage = "at.michaelfoidl.korm.integrationTests.generated.migrations",
            databasePath = "at.michaelfoidl.korm.integrationTests.generated.database",
            rootPackage = "at.michaelfoidl.korm.integrationTests.generated",
            rootDirectory = "src/main/generated"
    )

    private val configurationV1 = this.configurationCreator.createConfigurationForVersion(1, TestDatabaseV1::class)
    private val configurationV2 = this.configurationCreator.createConfigurationForVersion(2, TestDatabaseV2::class)
    private lateinit var databaseV1: TestDatabaseV1
    private lateinit var databaseV2: TestDatabaseV2

    private fun compileAndLoadMigration(fileName: String): Migration? {
        return BuildProcessFaker.compileAndLoadMigration(
                fileName,
                this.configurationCreator.rootDirectory,
                this.configurationCreator.migrationPackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configurationCreator.rootDirectory + "/../../../build/korm")
    }

    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
        return BuildProcessFaker.compileAndLoadDatabase(
                fileName,
                this.configurationCreator.rootDirectory,
                this.configurationCreator.databasePackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configurationCreator.rootDirectory + "/../../../build/korm")
    }

    @BeforeEach
    fun setup() {
        val sourceFileNameV1 = DatabaseCreator(this.configurationV1).createDatabase()
        this.databaseV1 = compileAndLoadDatabase(sourceFileNameV1)!!

        val sourceFileNameV2 = DatabaseCreator(this.configurationV2).createDatabase()
        this.databaseV2 = compileAndLoadDatabase(sourceFileNameV2)!!
    }

    @Test
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Act
        var result: Long = -1
        this.databaseV1.connect().executeInTransaction {
            result = MasterTable
                    .slice(MasterTable.version)
                    .selectAll()
                    .first()[MasterTable.version]
        }

        // Assert
        result shouldEqual 1
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
    fun database_migrating_shouldIncreaseVersion() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(EntityOneTable)
        )

        val targetSchema = DatabaseSchema(
                listOf(EntityOneTable, EntityTwoTable)
        )

        val migrationCreator = MigrationCreator(this.configurationV1)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)
        val migration = compileAndLoadMigration(sourceFileName)

        migration shouldNotBe null

        // Act
        var result: Long = -1
        this.databaseV2.connect().executeInTransaction {
            result = MasterTable
                    .slice(MasterTable.version)
                    .selectAll()
                    .first()[MasterTable.version]
        }

        // Assert
        result shouldEqual 2
    }
}