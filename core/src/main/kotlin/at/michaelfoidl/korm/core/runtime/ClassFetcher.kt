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

package at.michaelfoidl.korm.core.runtime

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.interfaces.Migration
import org.jetbrains.exposed.sql.Table
import java.io.File

internal class ClassFetcher(
        private val kormConfiguration: KormConfiguration
) {
    fun fetchTable(entityName: String): Table {
        val tableBuilder = IOOracle.getTableBuilder(entityName, this.kormConfiguration)
        return ClassLoader(File(tableBuilder.buildPath(true)), true)
                .objectInstance<Table>(tableBuilder.qualifiedName())!!
    }

    fun fetchMigration(databaseConfiguration: DatabaseConfiguration): Migration {
        val migrationBuilder: IOBuilder = IOOracle.getMigrationBuilder(databaseConfiguration, this.kormConfiguration)
        return ClassLoader(File(migrationBuilder.buildPath(true)), true)
                .createInstance<Migration>(migrationBuilder.qualifiedName())!!
    }
}