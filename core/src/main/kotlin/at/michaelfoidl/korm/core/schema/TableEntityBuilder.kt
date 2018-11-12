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

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.core.exceptions.DatabaseSchemaException
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

internal class TableEntityBuilder(
        private val entity: KClass<*>
) : TableBuilder() {

    private val entityAnnotation: Entity? = this.entity.findAnnotation()

    private val allColumnBuilders: Collection<ColumnBuilder>
        get() {
            return this.entity.memberProperties.map { ColumnEntityBuilder(it) }
        }

    override val columnBuilders: Collection<ColumnBuilder>
        get() {
            return this.allColumnBuilders.filter { !it.isForeignKey && !it.isPrimaryKey }
        }

    override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
        get() {
            return this.allColumnBuilders
                    .filter { it.isForeignKey }
                    .map { it.asForeignKeyBuilder() }
        }

    override val primaryKeyBuilder: PrimaryKeyBuilder
        get() {
            return (this.allColumnBuilders.find { it.isPrimaryKey }
                    ?: throw DatabaseSchemaException("Entity ${this.entity.qualifiedName} has no primary key."))
                    .asPrimaryKeyBuilder()
        }

    override fun getName(): String {
        return if (this.entityAnnotation?.tableName == null || this.entityAnnotation.tableName.isBlank()) {
            this.entity.simpleName.toString().decapitalize()
        } else {
            this.entityAnnotation.tableName
        }
    }

    companion object {
        fun create(entity: KClass<*>): TableEntityBuilder {
            return TableEntityBuilder(entity)
        }
    }
}