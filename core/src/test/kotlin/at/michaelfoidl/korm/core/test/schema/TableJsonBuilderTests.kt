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

package at.michaelfoidl.korm.core.test.schema

import at.michaelfoidl.korm.core.schema.*
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test

class TableJsonBuilderTests {

    private val referencedPrimaryKey = PrimaryKey("primaryKeyColumn", true)
    private val referencedTable = Table("entityToBeReferenced", emptyList(), this.referencedPrimaryKey, emptyList())

    private fun provideTables(): Collection<Table> {
        return listOf(
                Table("myTable", emptyList(), PrimaryKey("myPrimaryKey", true), emptyList()),
                this.referencedTable,
                Table("mySecondTable", emptyList(), PrimaryKey("mySecondPrimaryKey", false), emptyList())
        )
    }

    private fun provideColumns(): Collection<Column> {
        return listOf(
                Column("myColumn", DatabaseType.Integer, false, false, false),
                this.referencedPrimaryKey,
                Column("myForeignKey", DatabaseType.Varchar, true, false, false)
        )
    }

    private val tableJson = """
        |{
        |  "name": "myEntity",
        |  "columns": [],
        |  "primaryKey": {
        |    "name": "id",
        |    "isAutoIncrement": true
        |  },
        |  "foreignKeys": [
        |    {
        |      "name": "myForeignKey",
        |      "isNullable": false,
        |      "referencedTable": "entityToBeReferenced",
        |      "referencedColumn": "primaryKeyColumn"
        |    }
        |  ]
        |}
    """.trimMargin()

    @Test
    fun tableJsonBuilder_nameOfTable_shouldBeCorrespondingJsonValue() {

        // Arrange
        val builder = TableJsonBuilder(Parser().parse(StringBuilder(this.tableJson)) as JsonObject)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "myEntity"
    }

    @Test
    fun tableJsonBuilder_toTable_shouldReturnCorrespondingTable() {

        // Arrange
        val builder = TableJsonBuilder(Parser().parse(StringBuilder(this.tableJson)) as JsonObject)

        // Act
        val result = builder.toTable(provideTables(), provideColumns())

        // Assert
        result shouldNotBe null
        result.name shouldEqual "myEntity"
        result.columns.count() shouldEqual 0
        result.foreignKeys.count() shouldEqual 1
        result.primaryKey shouldNotBe null
    }
}