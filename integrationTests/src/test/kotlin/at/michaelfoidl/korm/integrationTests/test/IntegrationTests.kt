package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.migrations.MasterTable
import at.michaelfoidl.korm.integrationTests.database.TestDatabase
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.Test

class IntegrationTests {

    @Test
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Arrange
        val db = TestDatabase()

        // Act
        db.connect()
        var result: Long = -1
        db.connect().executeInTransaction {
            result = MasterTable
                    .slice(MasterTable.version)
                    .selectAll()
                    .first()[MasterTable.version]
        }

        // Assert
        result shouldEqual 1
    }
}