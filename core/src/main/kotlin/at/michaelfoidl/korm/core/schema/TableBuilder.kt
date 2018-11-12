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

internal abstract class TableBuilder {
    abstract fun getName(): String

    protected abstract val columnBuilders: Collection<ColumnBuilder>
    protected abstract val foreignKeyBuilders: Collection<ForeignKeyBuilder>
    protected abstract val primaryKeyBuilder: PrimaryKeyBuilder

    fun canBeResolved(tables: Collection<Table>): Boolean {
        return !this.foreignKeyBuilders.any {
            it.asForeignKeyBuilder().getReferencedTable(tables) == null
        }
    }

    fun toTable(tables: Collection<Table>, columns: Collection<Column>): Table {
        val primaryKey = this.primaryKeyBuilder.asPrimaryKeyBuilder().toPrimaryKey()
        val normalColumns = this.columnBuilders
                .map { it.toColumn() }
                .toList()
        val foreignKeys = this.foreignKeyBuilders
                .map { it.asForeignKeyBuilder().toForeignKey(tables, columns) }
                .toList()
        return Table(
                getName(),
                normalColumns,
                primaryKey,
                foreignKeys)
    }
}