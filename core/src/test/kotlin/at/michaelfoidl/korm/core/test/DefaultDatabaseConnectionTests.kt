package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DefaultDatabaseConnection
import at.michaelfoidl.korm.core.exceptions.DoubleInitializationException
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class DefaultDatabaseConnectionTests {
    @Test
    fun uninitializedDatabaseConnection_isValid_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection()

        // Act
        val result = connection.isValid

        // Assert
        result shouldBe false
    }

    @Test
    fun initializedDatabaseConnection_isValid_shouldBeTrue() {

        // Arrange
        val connection = DefaultDatabaseConnection(mock())

        // Act
        val result = connection.isValid

        // Assert
        result shouldBe true
    }

    @Test
    fun executedDatabaseConnection_isValid_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection(mock())

        // Act
        connection.executeInTransaction { }
        val result = connection.isValid

        // Assert
        result shouldBe false
    }

    @Test
    fun closedDatabaseConnection_isValid_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection(mock())

        // Act
        connection.close()
        val result = connection.isValid

        // Assert
        result shouldBe false
    }

    @Test
    fun databaseConnection_initializingTwice_shouldThrowException() {

        // Arrange
        val connection = DefaultDatabaseConnection(mock())

        // Act
        val function = { connection.initialize(mock()) }

        // Assert
        function shouldThrow DoubleInitializationException::class
    }
}