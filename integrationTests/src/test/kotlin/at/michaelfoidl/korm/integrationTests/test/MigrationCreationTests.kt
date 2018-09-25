package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.configuration.DatabaseType
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.database.Database
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.migrations.MasterTable
import at.michaelfoidl.korm.integrationTests.database.TestDatabaseV1
import at.michaelfoidl.korm.testUtils.ClassLoader
import at.michaelfoidl.korm.testUtils.Compiler
import at.michaelfoidl.korm.testUtils.PackageDirectoryConverter
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import java.io.File
import java.nio.file.Paths

class MigrationCreationTests {

    private val configuration: KormConfiguration = KormConfiguration(
            DatabaseType.SQLite,
            1,
            TestDatabaseV1::class,
            "",
            "",
            "",
            "at.michaelfoidl.korm.integrationTest.test.generated.migrations",
            "at.michaelfoidl.korm.integrationTest.test.generated.database",
            "at.michaelfoidl.korm.core.integrationTest.test",
            "build/tmp/test/src"
    )

    private fun getDatabasePath(): String {
        return listOf(
                Paths.get("").toAbsolutePath().toString(),
                this.configuration.rootDirectory,
                PackageDirectoryConverter.convertPackageToDirectoryStructure(this.configuration.databasePackage),
                "Database.kt"
        ).joinToString("/")
    }

    private fun compileDatabase(buildFolderPath: String): Boolean {
        val sourceFilePath = getDatabasePath()
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    private inline fun <reified T : Database> loadDatabase(buildFolderPath: String): T {
        return ClassLoader(File(buildFolderPath)).createInstance<T>(this.configuration.databasePackage + ".Database")!!
    }
    private lateinit var database: TestDatabaseV1

    @BeforeEach
    fun setup() {
        DatabaseCreator(this.configuration).createDatabase()

        val buildFolderPath = this.configuration.rootDirectory + "/..build"

        compileDatabase(buildFolderPath)
        this.database = loadDatabase(buildFolderPath)
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Act
        var result: Long = -1
        this.database.connect().executeInTransaction {
            result = MasterTable
                    .slice(MasterTable.version)
                    .selectAll()
                    .first()[MasterTable.version]
        }

        // Assert
        result shouldEqual 1
    }

//    @Test
//    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
//    fun database_migrating_shouldIncreaseVersion() {
//
//        // Arrange
//        val currentSchema = DatabaseSchema(
//                listOf(EntityOneTable)
//        )
//
//        val targetSchema = DatabaseSchema(
//                listOf(EntityOneTable, EntityTwoTable)
//        )
//
//        val migrationCreator: MigrationCreator = MigrationCreator(this.configuration)
//        migrationCreator.createMigration(currentSchema, targetSchema)
//
//        // Act
//        var result: Long = -1
//        this.database.connect().executeInTransaction {
//            result = MasterTable
//                    .slice(MasterTable.version)
//                    .selectAll()
//                    .first()[MasterTable.version]
//        }
//
//        // Assert
//        result shouldEqual 2
//
//    }
}