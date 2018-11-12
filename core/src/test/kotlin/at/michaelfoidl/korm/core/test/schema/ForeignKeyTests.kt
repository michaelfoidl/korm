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

import at.michaelfoidl.korm.core.schema.DatabaseType
import at.michaelfoidl.korm.core.schema.ForeignKey
import at.michaelfoidl.korm.core.schema.PrimaryKey
import at.michaelfoidl.korm.core.schema.Table
import at.michaelfoidl.korm.core.testUtils.minify
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class ForeignKeyTests {

    private val primaryKey = PrimaryKey("id", true)
    private val table = Table("table", emptyList(), primaryKey, emptyList())
    private val foreignKey = ForeignKey("myForeignKey", true, table, primaryKey)
    private val foreignKeyJSON = """
        |{
        |  "name": "myForeignKey",
        |  "dataType": "${DatabaseType.Long}",
        |  "isNullable": true,
        |  "isAutoIncrement": false,
        |  "isIndexed": true,
        |  "referencedTable": "table",
        |  "referencedColumn": "id"
        |}
    """.trimMargin()

    @Test
    fun foreignKey_toJson_shouldReturnValidJson() {

        // Act
        val result = this.foreignKey.toJSON().minify()

        // Assert
        result shouldEqual this.foreignKeyJSON.minify()
    }

    @Test
    fun foreignKey_toJson_shouldReturnPrettyJson() {

        // Act
        val result = this.foreignKey.toJSON()

        // Assert
        result shouldEqual this.foreignKeyJSON
    }
}