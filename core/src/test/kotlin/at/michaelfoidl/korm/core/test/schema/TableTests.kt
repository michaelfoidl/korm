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
import at.michaelfoidl.korm.core.testUtils.minify
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer

class TableTests {

    private val table1Column1: PrimaryKey = mock()
    private val table1Column2: Column = mock()
    private val table1 = Table("table1", listOf(table1Column2), table1Column1, emptyList())
    private val table2Column1: PrimaryKey = mock()
    private val table2Column2: Column = mock()
    private val table2Column3: ForeignKey = mock()
    private val table2Column4: Column = mock()
    private val table2 = Table("table2", listOf(table2Column2, table2Column4), table2Column1, listOf(table2Column3))

    private val table1JSON = """
            |{
            |  "name": "table1",
            |  "columns": [
            |    {
            |      "name": "column2",
            |      "dataType": ${DatabaseType.Boolean},
            |      "isNullable": true,
            |      "isAutoIncrement": false,
            |      "isIndexed": false
            |    }
            |  ],
            |  "primaryKey": {
            |    "name": "column1",
            |    "isAutoIncrement": true,
            |  },
            |  "foreignKeys": []
            |}
        """.trimMargin()

    private val table2JSON = """
            |{
            |  "name": "table2",
            |  "columns": [
            |    {
            |      "name": "column2",
            |      "dataType": ${DatabaseType.Long},
            |      "isNullable": true,
            |      "isAutoIncrement": false,
            |      "isIndexed": false
            |    },
            |    {
            |      "name": "column4",
            |      "dataType": ${DatabaseType.Long},
            |      "isNullable": true,
            |      "isAutoIncrement": false,
            |      "isIndexed": false
            |    }
            |  ],
            |  "primaryKey": {
            |    "name": "column1",
            |    "isAutoIncrement": true,
            |  },
            |  "foreignKeys": [
            |    {
            |      "name": "column3",
            |      "isNullable": true,
            |      "referencedTable": "table1",
            |      "referencedColumn": "column1"
            |    }
            |  ]
            |}
        """.trimMargin()

    @BeforeEach
    fun setup() {
        When calling this.table1Column1.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column1",
                |  "isAutoIncrement": true,
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table1Column1.name itReturns "column1"

        When calling this.table1Column2.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column2",
                |  "dataType": ${DatabaseType.Boolean},
                |  "isNullable": true,
                |  "isAutoIncrement": false,
                |  "isIndexed": false
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table1Column2.name itReturns "column2"

        When calling this.table2Column1.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column1",
                |  "isAutoIncrement": true,
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table2Column1.name itReturns "column1"

        When calling this.table2Column2.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column2",
                |  "dataType": ${DatabaseType.Long},
                |  "isNullable": true,
                |  "isAutoIncrement": false,
                |  "isIndexed": false
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table2Column2.name itReturns "column2"

        When calling this.table2Column3.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column3",
                |  "isNullable": true,
                |  "referencedTable": "table1",
                |  "referencedColumn": "column1"
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table2Column3.name itReturns "column2"

        When calling this.table2Column4.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "column4",
                |  "dataType": ${DatabaseType.Long},
                |  "isNullable": true,
                |  "isAutoIncrement": false,
                |  "isIndexed": false
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table2Column4.name itReturns "column2"
    }

    @Test
    fun tableWithoutForeignKeys_toJson_shouldReturnValidJson() {

        // Act
        val result = table1.toJSON().minify()

        // Assert
        result shouldEqual table1JSON.minify()
    }

    @Test
    fun tableWithoutForeignKeys_toJson_shouldReturnPrettyJson() {

        // Act
        val result = table1.toJSON()

        // Assert
        result shouldEqual table1JSON
    }

    @Test
    fun tableWithForeignKey_toJson_shouldReturnValidJson() {

        // Act
        val result = table2.toJSON().minify()

        // Assert
        result shouldEqual table2JSON.minify()
    }

    @Test
    fun tableWithForeignKey_toJson_shouldReturnPrettyJson() {

        // Act
        val result = table2.toJSON()

        // Assert
        result shouldEqual table2JSON
    }
}