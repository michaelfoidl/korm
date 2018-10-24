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
        val propertyStream: InputStream? = ClassLoader.getSystemResourceAsStream(IOOracle.getKormConfigurationPropertyFileName())
        return if (propertyStream != null) {
            val properties = Properties()
            properties.load(propertyStream)
            DefaultKormConfiguration.fromProperties(properties)
        } else {
            DefaultKormConfiguration()
        }
    }

    fun provideDatabaseConfiguration(databaseInterface: KClass<out Database>): DatabaseConfiguration {
        val propertyStream: InputStream? = ClassLoader.getSystemResourceAsStream(IOOracle.getDatabaseConfigurationPropertyFileName(databaseInterface))
        return if (propertyStream != null) {
            val properties = Properties()
            properties.load(propertyStream)
            DefaultDatabaseConfiguration.fromProperties(properties)
        } else {
            DefaultDatabaseConfiguration()
        }
    }

}