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

class Table(
        val name: String,
        val columns: Collection<Column>,
        val primaryKey: PrimaryKey,
        val foreignKeys: Collection<ForeignKey>
) {
    val allColumns: Collection<Column>
        get() = this.columns
                .plus(this.primaryKey)
                .plus(this.foreignKeys)

    fun toJSON(): String {
        return toRawJSON(0).trimMargin()
    }

    internal fun toRawJSON(indent: Int = 0): String {
        return """
            |{
            |  "name": "$name",
            |  "columns": ${if (columns.isEmpty()) "[]" else """[
            |    ${columns.joinToString(",\n") { it.toRawJSON(indent + 4) }.substring(4)}
            |  ]"""},
            |  "primaryKey": ${this.primaryKey.toRawJSON(indent + 2).substring(2)},
            |  "foreignKeys": ${if (foreignKeys.isEmpty()) "[]" else """[
            |    ${foreignKeys.joinToString(",\n") { it.toRawJSON(indent + 4).substring(4) }}
            |  ]"""}
            |}
        """.trimMargin().prependIndent(" ".repeat(indent))
    }
}