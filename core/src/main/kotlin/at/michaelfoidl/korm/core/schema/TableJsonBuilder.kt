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

import com.beust.klaxon.JsonObject

internal class TableJsonBuilder(
        private val jsonObject: JsonObject
) : TableBuilder() {

    override val columnBuilders: Collection<ColumnJsonBuilder>
        get() = this.jsonObject
                .array<JsonObject>("columns")!!
                .map { ColumnJsonBuilder(it, false, false) }

    override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
        get() = this.jsonObject.array<JsonObject>("foreignKeys")!!
                .map { ForeignKeyJsonBuilder(it) }

    override val primaryKeyBuilder: PrimaryKeyBuilder
        get() = PrimaryKeyJsonBuilder(this.jsonObject.getValue("primaryKey") as JsonObject)

    override fun getName(): String {
        return this.jsonObject.string("name")!!
    }

    companion object {
        fun create(jsonObject: JsonObject): TableJsonBuilder {
            return TableJsonBuilder(jsonObject)
        }
    }
}