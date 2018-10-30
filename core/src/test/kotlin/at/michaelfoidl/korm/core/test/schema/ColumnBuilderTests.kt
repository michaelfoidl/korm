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

import at.michaelfoidl.korm.core.schema.ColumnBuilder
import at.michaelfoidl.korm.core.schema.DatabaseType
import org.junit.jupiter.api.Test
import at.michaelfoidl.korm.core.testUtils.entities.TestEntity
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe

class ColumnBuilderTests {
    @Test
    fun columnBuilder_nameOfColumnWithColumnNameAnnotation_shouldUseAnnotationValue() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::columnWithColumnNameAnnotation)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "myColumn"
    }

    @Test
    fun columnBuilder_nameOfColumnWithEmptyColumnNameAnnotation_shouldUsePropertyName() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::columnWithEmptyColumnNameAnnotation)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "columnWithEmptyColumnNameAnnotation"
    }

    @Test
    fun columnBuilder_nameOfColumnWithoutColumnNameAnnotation_shouldUsePropertyName() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "defaultColumn"
    }

    @Test
    fun columnBuilder_isNullable_shouldBeFalseByDefault() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.isNullable()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isNullable_shouldBeTrueForAnnotated() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::nullableColumn)

        // Act
        val result = builder.isNullable()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isNullable_shouldBeAlwaysFalseForPrimaryKeys() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::nullablePrimaryKeyColumn)

        // Act
        val result = builder.isNullable()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeFalseByDefault() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeTrueForAnnotated() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::autoIncrementedColumn)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeFalseForForeignKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::foreignKeyColumn)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeTrueForDefaultPrimaryKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumn)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeTrueForPrimaryKeyWithAutoIncrement() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumnWithAutoIncrement)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isAutoIncrement_shouldBeFalseForPrimaryKeyWithNoAutoIncrement() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumnWithNoAutoIncrement)

        // Act
        val result = builder.isAutoIncrement()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isIndexed_shouldBeFalseForDefault() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.isIndexed()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isIndexed_shouldBeTrueForAnnotated() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::indexedColumn)

        // Act
        val result = builder.isIndexed()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isIndexed_shouldAlwaysBeTrueForForeignKeys() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::foreignKeyColumn)

        // Act
        val result = builder.isIndexed()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isIndexed_shouldAlwaysBeTrueForPrimaryKeys() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumn)

        // Act
        val result = builder.isIndexed()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isForeignKey_shouldBeFalseForDefault() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.isForeignKey()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isForeignKey_shouldBeFalseForPrimaryKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumn)

        // Act
        val result = builder.isForeignKey()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isForeignKey_shouldBeTrueForForeignKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::foreignKeyColumn)

        // Act
        val result = builder.isForeignKey()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_isPrimaryKey_shouldBeFalseForDefault() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::defaultColumn)

        // Act
        val result = builder.isPrimaryKey()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isPrimaryKey_shouldBeFalseForForeignKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::foreignKeyColumn)

        // Act
        val result = builder.isPrimaryKey()

        // Assert
        result shouldEqual false
    }

    @Test
    fun columnBuilder_isPrimaryKey_shouldBeTrueForPrimaryKey() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::primaryKeyColumn)

        // Act
        val result = builder.isPrimaryKey()

        // Assert
        result shouldEqual true
    }

    @Test
    fun columnBuilder_toColumn_shouldReturnCorrespondingColumn() {

        // Arrange
        val builder = ColumnBuilder(TestEntity::complexColumn)

        // Act
        val result = builder.toColumn()

        // Assert
        result shouldNotBe null
        result.name shouldEqual "coolColumn"
        result.dataType shouldEqual DatabaseType.Long
        result.isNullable shouldEqual true
        result.isAutoIncrement shouldEqual false
        result.isIndexed shouldEqual true
        result.isForeignKey shouldEqual false
        result.isPrimaryKey shouldEqual false
    }
}