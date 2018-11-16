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

package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import java.io.InputStream
import java.util.*
import kotlin.reflect.KClass

object ConfigurationProvider {
    fun provideKormConfiguration(): KormConfiguration {
        return provideKormConfiguration(IOOracle)
    }

    internal fun provideKormConfiguration(ioOracle: IOOracle = IOOracle): KormConfiguration {
        val properties: Properties? = loadKormConfigurationProperties(ioOracle)
        return if (properties != null) DefaultKormConfiguration.fromProperties(properties) else DefaultKormConfiguration()
    }

    internal fun loadKormConfigurationProperties(ioOracle: IOOracle = IOOracle): Properties? {
        val propertyStream: InputStream? = ClassLoader.getSystemResourceAsStream(ioOracle.getKormConfigurationPropertyFileName())
        return if (propertyStream != null) {
            val properties = Properties()
            properties.load(propertyStream)
            properties
        } else {
            null
        }
    }

    fun provideDatabaseConfiguration(databaseInterface: KClass<out Database>): DatabaseConfiguration {
        return provideDatabaseConfiguration(databaseInterface, IOOracle)
    }

    internal fun provideDatabaseConfiguration(databaseInterface: KClass<out Database>, ioOracle: IOOracle): DatabaseConfiguration {
        val properties: Properties? =  loadDatabaseConfigurationProperties(databaseInterface, ioOracle)
        return if (properties != null) DefaultDatabaseConfiguration.fromProperties(properties) else DefaultDatabaseConfiguration()
    }

    internal fun loadDatabaseConfigurationProperties(databaseInterface: KClass<out Database>, ioOracle: IOOracle = IOOracle): Properties? {
        val propertyStream: InputStream? = ClassLoader.getSystemResourceAsStream(ioOracle.getDatabaseConfigurationPropertyFileName(databaseInterface))
        return if (propertyStream != null) {
            val properties = Properties()
            properties.load(propertyStream)
            properties
        } else {
            null
        }
    }
}