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

internal class TableBuilder(
        private val entity: KClass<*>
) {
    private val entityAnnotation: Entity? = this.entity.findAnnotation()

    private val columnBuilders: Collection<ColumnBuilder>
        get() {
            return this.entity.memberProperties.map { ColumnBuilder(it) }
        }

    fun getName(): String {
        return this.entityAnnotation?.tableName ?: this.entity.simpleName.toString()
    }

    fun canBeResolved(tables: Collection<Table>): Boolean {
        return !this.columnBuilders.any {
            if (it.isForeignKey()) {
                it.asForeignKeyBuilder().getReferencedTable(tables) == null
            } else {
                false
            }
        }
    }

    fun toTable(tables: Collection<Table>, columns: Collection<Column>): Table {
        val primaryKey = this.columnBuilders.find { it.isPrimaryKey() }?.asPrimaryKeyBuilder()?.toPrimaryKey()
                ?: throw DatabaseSchemaException("Entity ${this.entity.qualifiedName} has no primary key.")
        val normalColumns = this.columnBuilders
                .asSequence()
                .filter { !it.isPrimaryKey() && !it.isForeignKey() }
                .map { it.toColumn() }
                .toList()
        val foreignKeys = this.columnBuilders
                .asSequence()
                .filter { it.isForeignKey() }
                .map { it.asForeignKeyBuilder().toForeignKey(tables, columns) }
                .toList()
        return Table(
                getName(),
                listOf(primaryKey, *(normalColumns.toTypedArray()), *(foreignKeys.toTypedArray())),
                primaryKey,
                foreignKeys)
    }
}