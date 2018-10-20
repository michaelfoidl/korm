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

import at.michaelfoidl.korm.core.configuration.ConfigurationCreator
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File

class ConfigurationCreatorTests {

    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
            kormPackage = "at.michaelfoidl.korm.core.test.generated",
            sourceDirectory = "build/tmp/test/src",
            buildDirectory = "build/tmp/test/build"
    )

    private val configurationBuilder: IOBuilder = IOOracle.getKormConfigurationBuilder(this.kormConfiguration)

    private fun compileConfiguration(fileName: String): Boolean {
        return BuildProcessFaker.compileConfiguration(
                fileName,
                this.configurationBuilder.sourcePath(true),
                this.configurationBuilder.buildPath(true))
    }

    private fun compileAndLoadConfiguration(fileName: String): KormConfiguration? {
        return BuildProcessFaker.compileAndLoadConfiguration(
                fileName,
                this.configurationBuilder.sourcePath(true),
                this.configurationBuilder.buildPath(true))
    }

    @Test
    fun configurationCreator_createConfiguration_shouldGenerateNewFile() {

        // Arrange
        val configurationCreator = ConfigurationCreator()

        // Act
        val result = configurationCreator.createKormConfiguration(this.kormConfiguration)

        // Assert
        File("${this.configurationBuilder.sourcePath()}/$result.kt").exists() shouldBe true
    }

    @Test
    fun configurationCreator_createConfiguration_shouldCompile() {

        // Arrange
        val configurationCreator = ConfigurationCreator()
        val sourceFileName = configurationCreator.createKormConfiguration(this.kormConfiguration)

        // Act
        val result = compileConfiguration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun configurationCreator_createConfiguration_shouldExtendDatabaseInterfaceClass() {

        /// Arrange
        val configurationCreator = ConfigurationCreator()
        val sourceFileName = configurationCreator.createKormConfiguration(this.kormConfiguration)

        // Act
        val result = compileAndLoadConfiguration(sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf KormConfiguration::class
    }
}