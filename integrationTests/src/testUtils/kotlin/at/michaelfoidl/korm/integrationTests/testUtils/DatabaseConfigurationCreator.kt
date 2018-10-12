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

package at.michaelfoidl.korm.integrationTests.testUtils

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import kotlin.reflect.KClass

class DatabaseConfigurationCreator(
        val databaseType: DatabaseType,
        val databasePath: String? = null,
        val username: String = "",
        val password: String = "",
        val migrationPackage: String = "migrations",
        val databasePackage: String = "database",
        val rootPackage: String,
        val rootDirectory: String = "src/main"
) {
    fun createConfigurationForVersion(version: Long, versionInterface: KClass<out Database>): KormConfiguration {
        return DefaultKormConfiguration(
                this.databaseType,
                version,
                versionInterface,
                "Database",
                this.databasePath,
                this.username,
                this.password,
                this.migrationPackage,
                this.databasePackage,
                this.rootPackage,
                this.rootDirectory
        )
    }
}