package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.DatabaseProvider
import at.michaelfoidl.korm.core.tables.MasterTable
import at.michaelfoidl.korm.interfaces.Database
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.Test

class MigrationCreationTests {

//    private val databaseConfigurationCreator = DatabaseConfigurationCreator()
//    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
//            migrationPackage = "migrations",
//            kormPackage = "at.michaelfoidl.korm.integrationTests.generated",
//            sourceDirectory = "",
//            buildDirectory = ""
//    )

//    private val databaseConfigurationV1 = this.databaseConfigurationCreator.createConfigurationForVersion(1)
//    private val databaseConfigurationV2 = this.databaseConfigurationCreator.createConfigurationForVersion(2)
//    private lateinit var databaseV1: TestDatabaseV1
//    private lateinit var databaseV2: TestDatabaseV2
//
//    private fun compileAndLoadMigration(fileName: String): Migration? {
//        return BuildProcessFaker.compileAndLoadMigration(
//                fileName,
//                this.kormConfiguration.sourceDirectory,
//                "${this.kormConfiguration.kormPackage}.${this.kormConfiguration.migrationPackage}",
//                this.kormConfiguration.buildDirectory)
//    }
//
//    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
//        return BuildProcessFaker.compileAndLoadDatabase(
//                fileName,
//                this.kormConfiguration.generatedSourceDirectory,
//                "${this.kormConfiguration.kormPackage}.database",
//                this.kormConfiguration.generatedBuildDirectory)
//    }

//    @BeforeEach
//    fun setup() {
//        val sourceFileNameV1 = DatabaseCreator(this.databaseConfigurationV1, this.kormConfiguration).createDatabase(ClassTypeWrapper(TestDatabaseV1::class))
//        this.databaseV1 = compileAndLoadDatabase(sourceFileNameV1)!!
//
//        val sourceFileNameV2 = DatabaseCreator(this.databaseConfigurationV2, this.kormConfiguration).createDatabase(ClassTypeWrapper(TestDatabaseV2::class))
//        this.databaseV2 = compileAndLoadDatabase(sourceFileNameV2)!!
//    }

    // TODO rebuild after each test

    @Test
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Arrange
        val database: Database = DatabaseProvider.provideDatabase("TestDatabase", 1)

        // Act
        var result: Long = -1
        database.connect().executeInTransaction {
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
//        val migrationCreator = MigrationCreator(this.databaseConfigurationV1, this.kormConfiguration)
//        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)
//        compileAndLoadMigration(sourceFileName)
//
//        // Act
//        var result: Long = -1
//        this.databaseV2.connect().executeInTransaction {
//            result = MasterTable
//                    .slice(MasterTable.version)
//                    .selectAll()
//                    .first()[MasterTable.version]
//        }
//
//        // Assert
//        result shouldEqual 2
//    }
}