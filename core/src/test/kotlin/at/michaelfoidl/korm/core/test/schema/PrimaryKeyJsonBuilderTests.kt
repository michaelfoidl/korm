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

import at.michaelfoidl.korm.core.schema.PrimaryKey
import at.michaelfoidl.korm.core.schema.PrimaryKeyJsonBuilder
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test

class PrimaryKeyJsonBuilderTests {

    @Test
    fun primaryKeyJsonBuilder_toPrimaryKey_shouldReturnCorrespondingPrimaryKey() {

        // Arrange
        val primaryKeyJson = """
            |{
            |  "name": "myColumn",
            |  "isAutoIncrement": false
            |}
        """.trimMargin()
        val builder = PrimaryKeyJsonBuilder(Parser().parse(StringBuilder(primaryKeyJson)) as JsonObject)

        // Act
        val result = builder.toPrimaryKey()

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf PrimaryKey::class
    }
}