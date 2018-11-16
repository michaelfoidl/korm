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

package at.michaelfoidl.korm.core.test.configuration

import at.michaelfoidl.korm.core.configuration.ConfigurationProvider
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConfigurationProviderTests {

    private val validIOOracle: IOOracle = mock()
    private val invalidIOOracle: IOOracle = mock()

    @BeforeEach
    fun setup() {
        When calling this.validIOOracle.getKormConfigurationPropertyFileName() itReturns "kormConfiguration.properties"
        When calling this.validIOOracle.getDatabaseConfigurationPropertyFileName(any()) itReturns "databaseConfiguration.properties"

        When calling this.invalidIOOracle.getKormConfigurationPropertyFileName() itReturns "invalidFile.properties"
        When calling this.invalidIOOracle.getDatabaseConfigurationPropertyFileName(any()) itReturns "invalidFile.properties"

    }

    @Test
    fun configurationProvider_loadKormConfigurationProperties_shouldReturnPropertiesForExistingFile() {

        // Act
        val result = ConfigurationProvider.loadKormConfigurationProperties(this.validIOOracle)

        // Assert
        result shouldNotBe null
        result!!["at.michaelfoidl.korm.kormPackage"] shouldEqual "at.michaelfoidl.korm.core.test"
    }

    @Test
    fun configurationProvider_loadKormConfigurationProperties_shouldReturnNullForInvalidFile() {

        // Act
        val result = ConfigurationProvider.loadKormConfigurationProperties(this.invalidIOOracle)

        // Assert
        result shouldBe null
    }

    @Test
    fun configurationProvider_loadDatabaseConfigurationProperties_shouldReturnPropertiesForExistingFile() {

        // Act
        val result = ConfigurationProvider.loadDatabaseConfigurationProperties(Database::class, this.validIOOracle)

        // Assert
        result shouldNotBe null
        result!!["at.michaelfoidl.korm.databaseName"] shouldEqual "MyDatabase"
    }

    @Test
    fun configurationProvider_loadDatabaseConfigurationProperties_shouldReturnNullForInvalidFile() {

        // Act
        val result = ConfigurationProvider.loadDatabaseConfigurationProperties(Database::class, this.invalidIOOracle)

        // Assert
        result shouldBe null
    }

    @Test
    fun configurationProvider_provideKormConfiguration_shouldAlwaysReturnKormConfiguration() {

        // Act
        val result = ConfigurationProvider.provideKormConfiguration(this.invalidIOOracle)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf KormConfiguration::class
    }

    @Test
    fun configurationProvider_provideDatabaseConfiguration_shouldAlwaysReturnDatabaseConfiguration() {

        // Act
        val result = ConfigurationProvider.provideDatabaseConfiguration(Database::class, this.invalidIOOracle)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf DatabaseConfiguration::class
    }
}