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

package at.michaelfoidl.korm.core.testUtils.entities

import at.michaelfoidl.korm.annotations.*

class TestEntity {
    val defaultColumn: String = ""

    @ColumnName("myColumn")
    val columnWithColumnNameAnnotation: String = ""

    @ColumnName("")
    val columnWithEmptyColumnNameAnnotation: String = ""

    @Nullable
    val nullableColumn: String? = null

    @PrimaryKey
    @Nullable
    val nullablePrimaryKeyColumn: Long = 0

    @AutoIncrement
    val autoIncrementedColumn: Long = 0

    @Indexed
    val indexedColumn: String = ""

    @ForeignKey(SimpleEntity1::class, "id")
    val foreignKeyColumn: Long = 0

    @PrimaryKey
    val primaryKeyColumn: Long = 0

    @PrimaryKey(autoIncrement = true)
    val primaryKeyColumnWithAutoIncrement: Long = 0

    @PrimaryKey(autoIncrement = false)
    val primaryKeyColumnWithNoAutoIncrement: Long = 0

    @Nullable
    @ColumnName("coolColumn")
    @Indexed
    val complexColumn: Long? = 0
}