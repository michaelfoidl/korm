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
import org.junit.jupiter.api.Test
import at.michaelfoidl.korm.core.testUtils.TestEntity
import org.amshove.kluent.shouldEqual

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
        val builder = ColumnBuilder(TestEntity::columnWithoutColumnNameAnnotation)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "columnWithoutColumnNameAnnotation"
    }
}