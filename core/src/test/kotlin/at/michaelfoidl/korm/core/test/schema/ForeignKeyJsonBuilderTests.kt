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
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class ForeignKeyJsonBuilderTests {

    private val referencedPrimaryKey = PrimaryKey("id", true)
    private val referencedTable = Table("otherTable", emptyList(), this.referencedPrimaryKey, emptyList())

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

    @Test
    fun foreignKeyJsonBuilder_referencedTable_shouldFindCorrectTable() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "otherTable",
            |  "referencedColumn": "id"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val result = builder.getReferencedTable(provideTables())

        // Assert
        result shouldNotBe null
        result shouldEqual this.referencedTable
    }

    @Test
    fun foreignKeyJsonBuilder_referencedTable_shouldReturnNullForUnknownTable() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "abc",
            |  "referencedColumn": "id"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val result = builder.getReferencedTable(provideTables())

        // Assert
        result shouldBe null
    }

    @Test
    fun foreignKeyJsonBuilder_referencedColumn_shouldFindCorrectColumn() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "otherTable",
            |  "referencedColumn": "id"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val result = builder.getReferencedColumn(provideColumns())

        // Assert
        result shouldNotBe null
        result shouldEqual this.referencedPrimaryKey
    }

    @Test
    fun foreignKeyJsonBuilder_referencedColumn_shouldReturnNullForUnknownColumn() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "otherTable",
            |  "referencedColumn": "abc"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val result = builder.getReferencedColumn(provideColumns())

        // Assert
        result shouldBe null
    }

    @Test
    fun foreignKeyJsonBuilder_toForeignKey_shouldReturnCorrespondingForeignKey() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "otherTable",
            |  "referencedColumn": "id"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val result = builder.toForeignKey(provideTables(), provideColumns())

        // Assert
        result shouldNotBe null
        result.referencedTable shouldEqual this.referencedTable
        result.referencedColumn shouldEqual this.referencedPrimaryKey
    }

    @Test
    fun foreignKeyJsonBuilder_toForeignKey_shouldFailWithUnknownReferencedTable() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "abc",
            |  "referencedColumn": "id"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val function = { builder.toForeignKey(provideTables(), provideColumns()) }

        // Assert
        function shouldThrow IllegalArgumentException::class
    }

    @Test
    fun foreignKeyJsonBuilder_toForeignKey_shouldFailWithUnknownReferencedColumn() {

        // Arrange
        val foreignKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isNullable": false,
            |  "referencedTable": "otherTable",
            |  "referencedColumn": "abc"
            |}
        """.trimMargin()
        val builder = ForeignKeyJsonBuilder(Parser().parse(StringBuilder(foreignKeyJson)) as JsonObject)

        // Act
        val function = { builder.toForeignKey(provideTables(), provideColumns()) }

        // Assert
        function shouldThrow IllegalArgumentException::class
    }
}