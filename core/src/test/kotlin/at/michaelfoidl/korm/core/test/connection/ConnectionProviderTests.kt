/*
 * korm
 *
 * Copyright (c) 2018, Michael Foidl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.michaelfoidl.korm.core.test.connection

import at.michaelfoidl.korm.core.connection.ConnectionProvider
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
    fun connectionProvider_provide_shouldReturnOpenConnection() {

        // Arrange
        val provider = ConnectionProvider(this.configuration)

        // Act
        val result = provider.provideConnection()

        // Assert
        result shouldNotBe null
        result.isOpen shouldBe true
    }

    @Test
    fun connectionProvider_provideTwiceWithoutUsingConnection_shouldReturnSameConnection() {

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

    @Test
    fun connectionProvider_providingAfterConfiguring_shouldReturnNewConnection() {

        // Arrange
        val provider = ConnectionProvider(this.configuration)

        // Act
        val result1 = provider.provideConnection()
        provider.configure(this.configuration)
        val result2 = provider.provideConnection()

        // Assert
        result1 shouldNotBe null
        result2 shouldNotBe null
        result1 shouldNotEqual result2
    }
}