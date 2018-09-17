package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.core.migrations.MasterTable
import at.michaelfoidl.korm.integrationTests.database.TestDatabase
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.sql.Connection

class IntegrationTests {

    @Test
    fun database_connectingForTheFirstTime_shouldCreateMasterTable() {

        // Arrange
        val db = TestDatabase()

        // Act
        db.connect()
        var result: Long = -1
        transaction(Connection.TRANSACTION_READ_UNCOMMITTED, 1) {
            result = MasterTable
                    .slice(MasterTable.version)
                    .selectAll()
                    .first()[MasterTable.version]
        }

        // Assert
        result shouldEqual 1
    }
}