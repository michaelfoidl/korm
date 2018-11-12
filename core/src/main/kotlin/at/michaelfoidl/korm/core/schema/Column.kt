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

package at.michaelfoidl.korm.core.schema

open class Column(
        val name: String,
        val dataType: DatabaseType,
        val isNullable: Boolean,
        val isAutoIncrement: Boolean,
        val isIndexed: Boolean
) {
    open val isPrimaryKey = false
    open val isForeignKey = false

    fun compareTo(other: Column): ColumnCompareResult {
        return compare(this, other)
    }

    open fun toJSON(): String {
        return toRawJSON(0).trimMargin()
    }

    internal open fun toRawJSON(indent: Int = 0): String {
        return """
            |{
            |  "name": "$name",
            |  "dataType": "$dataType",
            |  "isNullable": $isNullable,
            |  "isAutoIncrement": $isAutoIncrement,
            |  "isIndexed": $isIndexed
            |}
        """.trimMargin().prependIndent(" ".repeat(indent))
    }

    companion object {
        fun compare(first: Column, second: Column): ColumnCompareResult {
            if (first.name != second.name) {
                throw IllegalArgumentException("You must not compare columns with different names since renaming columns is not supported.")
            }
            return ColumnCompareResult(
                    first.dataType != second.dataType,
                    first.isNullable != second.isNullable,
                    first.isAutoIncrement != second.isAutoIncrement,
                    first.isIndexed != second.isIndexed)
        }
    }
}