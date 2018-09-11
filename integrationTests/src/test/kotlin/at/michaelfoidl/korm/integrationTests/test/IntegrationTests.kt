package at.michaelfoidl.korm.integrationTests.test

import at.michaelfoidl.korm.integrationTests.database.TestDatabase
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import test.EntityOneTable
import java.sql.Connection

class IntegrationTests {

    @Test
    fun database_connectingForTheFirstTime_shouldCreateDatabase() {

        // Arrange
        val db = TestDatabase()

        // Act
        db.connect()
        var result: Int = -1
        transaction(Connection.TRANSACTION_READ_UNCOMMITTED, 1) {
            result = EntityOneTable.selectAll().count()
        }

        // Assert
        result shouldEqual 0
    }
}