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

package at.michaelfoidl.korm.core.testUtils

import at.michaelfoidl.korm.core.schema.*
import at.michaelfoidl.korm.core.schema.Table

object TableProvider {
    fun simpleTable1(): Table {
        val idColumn = PrimaryKey("id", true)
        val nameColumn = Column("name", DatabaseType.Varchar, false, false, false)

        return Table("simpleTable1",
                listOf(idColumn, nameColumn),
                idColumn,
                emptyList())
    }

    fun simpleTable2(): Table {
        val idColumn = PrimaryKey("id", true)
        val nameColumn = Column("name", DatabaseType.Varchar, false, false, false)

        return Table("simpleTable2",
                listOf(idColumn, nameColumn),
                idColumn,
                emptyList())
    }
}