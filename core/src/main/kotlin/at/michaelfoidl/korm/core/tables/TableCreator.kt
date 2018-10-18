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

class TableCreator(
        private val kormConfiguration: KormConfiguration
) {
    fun createTable(element: TypeWrapper) {
        val tableBuilder: IOBuilder = IOOracle.getTableBuilder(element, kormConfiguration)
        FileSpec.builder(tableBuilder.packageName(), tableBuilder.simpleName())
                .addType(generateTableDefinitionFor(element))
                .build()
                .writeTo(File(tableBuilder.sourcePath(true), ""))
    }

    private fun generateTableDefinitionFor(element: TypeWrapper): TypeSpec {
        return TypeSpec.objectBuilder(IOOracle.getTableName(element))
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