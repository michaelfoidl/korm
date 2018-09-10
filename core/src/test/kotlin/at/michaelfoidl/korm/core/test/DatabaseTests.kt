package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.testUtils.OpenDatabase
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.*
import org.junit.jupiter.api.Test

class DatabaseTests {
    @Test
    fun database_connecting_shouldReturnNewConnection() {

        // Arrange
        val db: OpenDatabase = spy()
        doNothing().whenever(db).doConnect(any(), any(), any(), any())

        // Act
        val result = db.connect()

        // Assert
        result shouldNotBe null
    }

    @Test
    fun database_connectingTwice_shouldReturnSameConnection() {

        // Arrange
        val db: OpenDatabase = spy()
        doNothing().whenever(db).doConnect(any(), any(), any(), any())

        // Act
        val result1 = db.connect()
        val result2 = db.connect()

        // Assert
        result1 shouldNotBe null
        result2 shouldNotBe null
        result1 shouldEqual result2
    }

}