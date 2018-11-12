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

import at.michaelfoidl.korm.core.exposed.TypeAdapter
import com.beust.klaxon.JsonObject

internal open class ColumnJsonBuilder constructor(
        protected val jsonObject: JsonObject,
        override val isForeignKey: Boolean,
        override val isPrimaryKey: Boolean
) : ColumnBuilder {

    override fun getName(): String {
        return this.jsonObject.string("name")!!
    }

    override fun getType(): DatabaseType {
        return TypeAdapter.fromString(this.jsonObject.string("dataType")!!)
    }

    override fun isNullable(): Boolean {
        return this.jsonObject.boolean("isNullable")!!
    }

    override fun isAutoIncrement(): Boolean {
        return this.jsonObject.boolean("isAutoIncrement")!!
    }

    override fun isIndexed(): Boolean {
        return this.jsonObject.boolean("isIndexed")!!
    }

    override fun asForeignKeyBuilder(): ForeignKeyJsonBuilder {
        if (!this.isForeignKey) {
            throw IllegalStateException("Column '{${this.getName()}' does not represent a foreign key. Define it in the foreign key section.")
        }
        return ForeignKeyJsonBuilder(
                this.jsonObject)
    }

    override fun asPrimaryKeyBuilder(): PrimaryKeyJsonBuilder {
        if (!this.isPrimaryKey) {
            throw IllegalStateException("Column '{${this.getName()}' does not represent a primary key. Define it in the primary key section.")
        }
        return PrimaryKeyJsonBuilder(
                this.jsonObject)
    }
}