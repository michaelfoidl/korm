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

package at.michaelfoidl.korm.core.io

import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.TypeWrapper
import kotlin.reflect.KClass

internal object IOOracle {

    fun getMigrationBuilder(databaseConfiguration: DatabaseConfiguration, kormConfiguration: KormConfiguration): IOBuilder {
        return IOBuilder(kormConfiguration)
                .root()
                .kormRoot()
                .migration(IOBuilder.source, IOBuilder.build)
                .name(getMigrationName(databaseConfiguration.databaseName, databaseConfiguration.databaseVersion))
    }

    fun getDatabaseBuilder(databaseConfiguration: DatabaseConfiguration, kormConfiguration: KormConfiguration): IOBuilder {
        return IOBuilder(kormConfiguration)
                .root()
                .kormRoot()
                .database(IOBuilder.generatedSource, IOBuilder.generatedBuild)
                .name(getDatabaseName(databaseConfiguration.databaseName, databaseConfiguration.databaseVersion))
    }

    fun getTableBuilder(kormConfiguration: KormConfiguration): IOBuilder {
        return doGetTableBuilder(null, kormConfiguration)
    }

    fun getTableBuilder(entityName: String, kormConfiguration: KormConfiguration): IOBuilder {
        return doGetTableBuilder(getTableName(entityName), kormConfiguration)
    }

    fun getTableBuilder(element: TypeWrapper, kormConfiguration: KormConfiguration): IOBuilder {
        return doGetTableBuilder(getTableName(element), kormConfiguration)
    }

    private fun doGetTableBuilder(name: String? = null, kormConfiguration: KormConfiguration): IOBuilder {
        return IOBuilder(kormConfiguration)
                .root()
                .kormRoot()
                .table(IOBuilder.generatedSource, IOBuilder.generatedBuild)
                .name(name ?: "")
    }

    fun getKormConfigurationBuilder(kormConfiguration: KormConfiguration): IOBuilder {
        return IOBuilder(kormConfiguration)
                .root()
                .configuration(IOBuilder.generatedSource, IOBuilder.generatedBuild)
                .name(getKormConfigurationName())
    }

    fun getMigrationName(databaseName: String, databaseVersion: Long): String {
        return "${databaseName}_Migration_v${databaseVersion}_${databaseVersion + 1}"
    }

    fun getTableName(element: TypeWrapper): String {
        return ElementConverter.getSimpleName(element.typeName!!) + "Table"
    }

    fun getTableName(entityName: String): String {
        return entityName + "Table"
    }

    fun getDatabaseName(databaseName: String, databaseVersion: Long): String {
        return "${databaseName}_v$databaseVersion"
    }

    fun getKormConfigurationName(): String {
        return "CurrentKormConfiguration"
    }

    fun getKormConfigurationPropertyFileName(): String {
        return "korm.properties"
    }

    fun getDatabaseConfigurationPropertyFileName(databaseInterface: KClass<out Database>): String {
        return "${databaseInterface.qualifiedName}.properties"
    }
}