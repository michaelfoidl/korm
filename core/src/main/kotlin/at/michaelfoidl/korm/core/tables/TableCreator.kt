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

package at.michaelfoidl.korm.core.tables

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.TypeWrapper
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.exposed.sql.Table
import java.io.File

class TableCreator internal constructor(
        private val kormConfiguration: KormConfiguration,
        private val ioOracle: IOOracle = IOOracle
) {
    constructor(kormConfiguration: KormConfiguration) : this(kormConfiguration, IOOracle)

    fun createTable(element: TypeWrapper): String {
        val tableBuilder: IOBuilder = this.ioOracle.getTableBuilder(element, kormConfiguration)
        val tableName: String = tableBuilder.simpleName()
        FileSpec.builder(tableBuilder.packageName(), tableName)
                .addType(generateTableDefinitionFor(element))
                .build()
                .writeTo(File(tableBuilder.sourcePath(true), ""))
        return tableName
    }

    private fun generateTableDefinitionFor(element: TypeWrapper): TypeSpec {
        return TypeSpec.objectBuilder(this.ioOracle.getTableName(element))
                .superclass(Table::class.asTypeName())
                .addProperties(generateColumnDefinitionsFor(element))
                .build()
    }

    private fun generateColumnDefinitionsFor(element: TypeWrapper): Collection<PropertySpec> {
        val results: MutableCollection<PropertySpec> = ArrayList()

        element.fields.value.forEach { field ->
            results.add(ColumnCreator.createColumn(field))
        }

        return results
    }
}