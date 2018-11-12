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

import at.michaelfoidl.korm.core.schema.Column
import at.michaelfoidl.korm.core.schema.DatabaseType
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import at.michaelfoidl.korm.core.testUtils.minify

class ColumnTests {

    private val column = Column("myColumn", DatabaseType.Long, false, false, false)
    private val columnJSON = """
        |{
        |  "name": "myColumn",
        |  "dataType": "${DatabaseType.Long}",
        |  "isNullable": false,
        |  "isAutoIncrement": false,
        |  "isIndexed": false
        |}
    """.trimMargin()

    @Test
    fun column_compare_shouldDetectNoChangesForEqualColumns() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Varchar, false, false, false)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe false
        result.isNullableDifferent shouldBe false
        result.isAutoIncrementDifferent shouldBe false
        result.isIndexedDifferent shouldBe false
    }

    @Test
    fun column_compare_shouldDetectDifferentDataType() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Integer, false, false, false)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe true
        result.isNullableDifferent shouldBe false
        result.isAutoIncrementDifferent shouldBe false
        result.isIndexedDifferent shouldBe false
    }


    @Test
    fun column_compare_shouldDetectDifferentNullability() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Varchar, true, false, false)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe false
        result.isNullableDifferent shouldBe true
        result.isAutoIncrementDifferent shouldBe false
        result.isIndexedDifferent shouldBe false
    }

    @Test
    fun column_compare_shouldDetectDifferentAutoIncrementation() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Varchar, false, true, false)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe false
        result.isNullableDifferent shouldBe false
        result.isAutoIncrementDifferent shouldBe true
        result.isIndexedDifferent shouldBe false
    }

    @Test
    fun column_compare_shouldDetectDifferentIndexation() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Varchar, false, false, true)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe false
        result.isNullableDifferent shouldBe false
        result.isAutoIncrementDifferent shouldBe false
        result.isIndexedDifferent shouldBe true
    }

    @Test
    fun column_compare_shouldDetectMultipleChanges() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Long, true, false, true)

        // Act
        val result = Column.compare(column1, column2)

        // Assert
        result.isTypeDifferent shouldBe true
        result.isNullableDifferent shouldBe true
        result.isAutoIncrementDifferent shouldBe false
        result.isIndexedDifferent shouldBe true
    }

    @Test
    fun column_compare_shouldWorkWithMemberFunction() {

        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("myColumn", DatabaseType.Long, true, false, true)

        // Act
        val result1 = Column.compare(column1, column2)
        val result2 = column1.compareTo(column2)

        // Assert
        result1.isTypeDifferent shouldEqual result2.isTypeDifferent
        result1.isNullableDifferent shouldEqual result2.isNullableDifferent
        result1.isAutoIncrementDifferent shouldEqual result2.isAutoIncrementDifferent
        result1.isIndexedDifferent shouldEqual result2.isIndexedDifferent
    }

    @Test
    fun column_compareWithDifferentNames_shouldFail() {
        // Arrange
        val column1 = Column("myColumn", DatabaseType.Varchar, false, false, false)
        val column2 = Column("yourColumn", DatabaseType.Varchar, false, false, false)

        // Act
        val function = { Column.compare(column1, column2) }

        // Assert
        function shouldThrow IllegalArgumentException::class
    }

    @Test
    fun column_toJson_shouldReturnValidJson() {

        // Act
        val result = column.toJSON().minify()

        // Assert
        result shouldEqual columnJSON.minify()
    }

    @Test
    fun column_toJson_shouldReturnPrettyJson() {

        // Act
        val result = column.toJSON()

        // Assert
        result shouldEqual columnJSON
    }
}