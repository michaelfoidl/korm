package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.DatabaseProvider
import at.michaelfoidl.korm.core.tables.MasterTable
import at.michaelfoidl.korm.integrationTests.database.TestDatabaseV1
import at.michaelfoidl.korm.interfaces.Database
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.Test

class MigrationCreationTests {

    // TODO rebuild after each test

    @Test
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Arrange
        val database: Database = DatabaseProvider.provideDatabase(TestDatabaseV1::class)

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