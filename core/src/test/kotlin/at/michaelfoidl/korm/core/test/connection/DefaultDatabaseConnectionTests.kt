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

import at.michaelfoidl.korm.core.connection.DefaultDatabaseConnection
import com.zaxxer.hikari.HikariDataSource
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class DefaultDatabaseConnectionTests {

    private val dataSource: HikariDataSource = mock()

    @Test
    fun newDatabaseConnection_isOpen_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection()

        // Act
        val result = connection.isOpen

        // Assert
        result shouldBe false
    }

    @Test
    fun openedDatabaseConnection_isOpen_shouldBeTrue() {

        // Arrange
        val connection = DefaultDatabaseConnection().open(this.dataSource)

        // Act
        val result = connection.isOpen

        // Assert
        result shouldBe true
    }

    @Test
    fun executedDatabaseConnection_isOpen_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection().open(this.dataSource)

        // Act
        connection.executeInTransaction { }
        val result = connection.isOpen

        // Assert
        result shouldBe false
    }

    @Test
    fun closedDatabaseConnection_isOpen_shouldBeFalse() {

        // Arrange
        val connection = DefaultDatabaseConnection().open(this.dataSource)

        // Act
        connection.close()
        val result = connection.isOpen

        // Assert
        result shouldBe false
    }

    @Test
    fun closedDatabaseConnection_execute_shouldFail() {

        // Arrange
        val connection = DefaultDatabaseConnection()

        // Act
        val func =  { connection.executeInTransaction {  } }

        // Assert
        func shouldThrow IllegalStateException::class

    }
}