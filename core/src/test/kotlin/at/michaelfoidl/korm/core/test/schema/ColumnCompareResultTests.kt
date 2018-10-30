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
import at.michaelfoidl.korm.core.schema.ColumnCompareResult
import at.michaelfoidl.korm.core.schema.DatabaseType
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class ColumnCompareResultTests {
    @Test
    fun columnCompareResult_isUnchanged_shouldBeTrueForUnchangedResult() {

        // Arrange
        val compareResult = ColumnCompareResult(false, false, false, false)

        // Act
        val result = compareResult.isUnchanged

        // Assert
        result shouldBe true
    }

    @Test
    fun columnCompareResult_isUnchanged_shouldBeFalseIfThereAreAnyChanged() {

        // Arrange
        val compareResult = ColumnCompareResult(false, true, false, false)

        // Act
        val result = compareResult.isUnchanged

        // Assert
        result shouldBe false
    }
}