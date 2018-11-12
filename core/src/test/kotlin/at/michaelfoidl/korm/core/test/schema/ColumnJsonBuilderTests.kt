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

import at.michaelfoidl.korm.core.schema.ColumnJsonBuilder
import at.michaelfoidl.korm.core.schema.DatabaseType
import at.michaelfoidl.korm.core.schema.ForeignKeyJsonBuilder
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class ColumnJsonBuilderTests {

    private val columnJson = """
            |{
            |  "name": "myColumn",
            |  "dataType": "${DatabaseType.Long}",
            |  "isNullable": false,
            |  "isAutoIncrement": false,
            |  "isIndexed": true
            |}
        """.trimMargin()

    @Test
    fun columnJsonBuilder_nameOfColumn_shouldBeCorrespondingJsonValue() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "myColumn"
    }

    @Test
    fun columnJsonBuilder_isNullable_shouldBeCorrespondingJsonValue() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.isNullable()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnJsonBuilder_isAutoIncrement_shouldBeCorrespondingJsonValue() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnJsonBuilder_isIndexed_shouldBeCorrespondingJsonValue() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.isIndexed()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnJsonBuilder_isForeignKey_shouldBeFalseByDefault() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.isForeignKey

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnJsonBuilder_isPrimaryKey_shouldBeFalseByDefault() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.isPrimaryKey

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnJsonBuilder_toColumn_shouldReturnCorrespondingColumn() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val result = builder.toColumn()

        // Assert
        result shouldNotBe null
        result.name shouldEqual "myColumn"
        result.dataType shouldEqual DatabaseType.Long
        result.isNullable shouldEqual false
        result.isAutoIncrement shouldEqual false
        result.isIndexed shouldEqual true
        result.isForeignKey shouldEqual false
        result.isPrimaryKey shouldEqual false
    }

    @Test
    fun columnJsonBuilder_asForeignKeyBuilder_shouldReturnCorrespondingForeignKeyJsonBuilderForForeignKey() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, true, false)


        // Act
        val result = builder.asForeignKeyBuilder()

        // Assert
        result shouldNotBe null
        result.getName() shouldEqual "myColumn"
        result.getType() shouldEqual DatabaseType.Long
        result.isNullable() shouldEqual false
        result.isAutoIncrement() shouldEqual false
        result.isIndexed() shouldEqual true
        result.isForeignKey shouldEqual true
        result.isPrimaryKey shouldEqual false
    }

    @Test
    fun columnJsonBuilder_asForeignKeyBuilder_shouldFailForNonForeignKeyColumn() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val function = { builder.asForeignKeyBuilder() }

        // Assert
        function shouldThrow IllegalStateException::class
    }

    @Test
    fun columnJsonBuilder_asPrimaryKeyBuilder_shouldReturnCorrespondingPrimaryKeyJsonBuilderForPrimaryKey() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, true)

        // Act
        val result = builder.asPrimaryKeyBuilder()

        // Assert
        result shouldNotBe null
        result.getName() shouldEqual "myColumn"
        result.getType() shouldEqual DatabaseType.Long
        result.isNullable() shouldEqual false
        result.isAutoIncrement() shouldEqual false
        result.isIndexed() shouldEqual true
        result.isForeignKey shouldEqual false
        result.isPrimaryKey shouldEqual true
    }

    @Test
    fun columnJsonBuilder_asPrimaryKeyBuilder_shouldFailForNonPrimaryKeyColumn() {

        // Arrange
        val builder = ColumnJsonBuilder(Parser().parse(StringBuilder(columnJson)) as JsonObject, false, false)

        // Act
        val function = { builder.asPrimaryKeyBuilder() }

        // Assert
        function shouldThrow IllegalStateException::class
    }
}