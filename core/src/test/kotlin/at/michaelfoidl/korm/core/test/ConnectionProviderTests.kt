package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.ConnectionProvider
import com.zaxxer.hikari.HikariConfig
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConnectionProviderTests {

    private val configuration: HikariConfig = HikariConfig()

    @BeforeEach
    fun setup() {
        this.configuration.jdbcUrl = "jdbc:sqlite:file:test?mode=memory&cache=shared"
    }

    @Test
    fun connectionProvider_provide_shouldReturnNewConnection() {

        // Arrange
        val provider = ConnectionProvider(this.configuration)

        // Act
        val result = provider.provideConnection()

        // Assert
        result shouldNotBe null
    }

    @Test
    fun connectionProvider_provideTwice_shouldReturnSameConnection() {

        // Arrange
        val provider = ConnectionProvider(this.configuration)

        // Act
        val result1 = provider.provideConnection()
        val result2 = provider.provideConnection()

        // Assert
        result1 shouldNotBe null
        result2 shouldNotBe null
        result1 shouldEqual result2
    }

    @Test
    fun connectionProvider_provideAgainAfterUsingConnection_shouldReturnNewConnection() {

        // Arrange
        val provider = ConnectionProvider(this.configuration)

        // Act
        val result1 = provider.provideConnection()
        result1.executeInTransaction {  }
        val result2 = provider.provideConnection()

        // Assert
        result1 shouldNotBe null
        result2 shouldNotBe null
        result1 shouldNotEqual result2
    }

}