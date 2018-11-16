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

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class DefaultKormConfigurationTests {

    private val properties: Properties = Properties()

    @BeforeEach
    fun setup() {
        properties.load(ClassLoader.getSystemResourceAsStream("kormConfiguration.properties"))
    }

    @Test
    fun defaultKormConfiguration_fromProperties_shouldContainValuesFromPropertyFile() {

        // Act
        val result = DefaultKormConfiguration.fromProperties(this.properties)

        // Assert
        result.sourceDirectory shouldEqual ""
        result.buildDirectory shouldEqual ""
        result.rootDirectory shouldEqual ""
        result.kormPackage shouldEqual "at.michaelfoidl.korm.core.test"
        result.migrationPackage shouldEqual "migrations"
    }
}