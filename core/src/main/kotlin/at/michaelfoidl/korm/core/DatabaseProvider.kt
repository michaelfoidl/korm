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

package at.michaelfoidl.korm.core

import at.michaelfoidl.korm.core.configuration.ConfigurationProvider
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.runtime.ClassLoader
import at.michaelfoidl.korm.interfaces.Database
import java.io.File
import kotlin.reflect.KClass

object DatabaseProvider {
    fun provideDatabase(databaseName: String, databaseVersion: Long): Database {
        val databaseBuilder: IOBuilder = IOOracle.getDatabaseBuilder(databaseName, databaseVersion, ConfigurationProvider.provideKormConfiguration())
        return ClassLoader(File(databaseBuilder.buildPath(true)), true)
                .createInstance<Database>(databaseBuilder.qualifiedName())!!
    }
}