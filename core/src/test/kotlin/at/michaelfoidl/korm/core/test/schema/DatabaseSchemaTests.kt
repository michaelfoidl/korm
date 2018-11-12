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

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.schema.*
import at.michaelfoidl.korm.core.testUtils.schema.entities.Entity
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityToBeReferenced
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import org.mockito.stubbing.Answer
import kotlin.reflect.KClass
import at.michaelfoidl.korm.core.testUtils.minify

class DatabaseSchemaTests {

    private val tableBuilder: TableEntityBuilder.Companion = mock()
    private val ioOracle: IOOracle = mock()

    private val table1: Table = mock()
    private val table2: Table = mock()
    private val databaseSchema: DatabaseSchema = DatabaseSchema.fromTableCollection(listOf(table1, table2))
    private val databaseSchemaJSON = """
            |{
            |  "tables": [
            |    {
            |      "name": "table1",
            |      "columns": [],
            |      "primaryKey": {
            |        "name": "id",
            |        "isAutoIncrement": true,
            |      },
            |      "foreignKeys": []
            |    },
            |    {
            |      "name": "table2",
            |      "columns": [],
            |      "primaryKey": {
            |        "name": "id",
            |        "isAutoIncrement": true,
            |      },
            |      "foreignKeys": []
            |    }
            |  ]
            |}
        """

    @BeforeEach
    fun setup() {
        var i = 0
        When calling tableBuilder.create(any()) itAnswers Answer<TableEntityBuilder> {
            val builder: TableEntityBuilder = mock()

            if (i == 1) {
                When calling builder.canBeResolved(any()) itReturns false itReturns true

                val primaryKey = PrimaryKey("id", true)
                When calling builder.toTable(any(), any()) itReturns Table("table$i", emptyList(), primaryKey, emptyList())
            } else {
                When calling builder.canBeResolved(any()) itReturns true

                val primaryKey = PrimaryKey("id", true)
                When calling builder.toTable(any(), any()) itReturns Table("table$i", emptyList(), primaryKey, emptyList())
            }

            i++
            builder
        }

        When calling ioOracle.getDatabaseSchemaBuilder(any()) itAnswers Answer<IOBuilder> {
            val builder: IOBuilder = mock()

            When calling builder.sourcePath(any()) itReturns "src/testUtils/kotlin/at/michaelfoidl/korm/core/testUtils/schema/configuration/schema.json"

            builder
        }

        When calling this.table1.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "table1",
                |  "columns": [],
                |  "primaryKey": {
                |    "name": "id",
                |    "isAutoIncrement": true,
                |  },
                |  "foreignKeys": []
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }

        When calling this.table2.toRawJSON(any()) itAnswers Answer<String> {
            """
                |{
                |  "name": "table2",
                |  "columns": [],
                |  "primaryKey": {
                |    "name": "id",
                |    "isAutoIncrement": true,
                |  },
                |  "foreignKeys": []
                |}
            """.trimMargin().prependIndent(" ".repeat(it.getArgument(0)))
        }
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
    fun databaseSchema_fromConfigurationFile_shouldCreateCorrespondingDatabaseSchema() {

        // Act
        val result = DatabaseSchema.fromConfigurationFile(this.ioOracle)

        // Assert
        result shouldNotBe null
        result.tables.count() shouldEqual 2
        result.tables.flatMap { it.allColumns }.count() shouldEqual 3
    }

    @Test
    fun databaseSchema_fromEntityCollection_shouldCreateCorrespondingDatabaseSchema() {

        // Arrange
        val entities: Collection<KClass<*>> = listOf(Entity::class, EntityToBeReferenced::class)

        // Act
        val result = DatabaseSchema.fromEntityCollection(entities, this.tableBuilder)

        // Assert
        result shouldNotBe null
        result.tables.count() shouldEqual 2
        result.tables.flatMap { it.allColumns }.count() shouldEqual 2
    }

    @Test
    fun databaseSchema_toJSON_shouldReturnValidJSON() {

        // Act
        val result = this.databaseSchema.toJSON().minify()

        // Assert
        result shouldEqual this.databaseSchemaJSON.minify()
    }

    @Test
    fun databaseSchema_toJSON_shouldReturnPrettyJSON() {

        // Act
        val result = this.databaseSchema.toJSON()

        // Assert
        result shouldEqual this.databaseSchemaJSON.trimMargin()
    }
}