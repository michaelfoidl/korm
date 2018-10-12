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

import at.michaelfoidl.korm.core.io.ElementConverter
import at.michaelfoidl.korm.core.io.IOOracle
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.exposed.sql.Table
import java.io.File
import javax.lang.model.element.Element
import javax.lang.model.util.ElementFilter

class TableGenerator(
        private val rootPackage: String,
        private val rootDirectory: String
) {
    fun createTable(element: Element) {
        FileSpec.builder(IOOracle.getTablePackage(this.rootPackage), IOOracle.getTableName(element))
                .addType(generateTableDefinitionFor(element))
                .build()
                .writeTo(File(this.rootDirectory, ""))
    }

    private fun generateTableDefinitionFor(element: Element): TypeSpec {
        return TypeSpec.objectBuilder(IOOracle.getTableName(element))
                .superclass(Table::class.asTypeName())
                .addProperties(generateColumnDefinitionsFor(element))
                .build()
    }

    private fun generateColumnDefinitionsFor(element: Element): Collection<PropertySpec> {
        val results: MutableCollection<PropertySpec> = ArrayList()

        ElementFilter.fieldsIn(element.enclosedElements).forEach { field ->
            results.add(ColumnCreator.createColumn(field))
        }

        return results
    }
}