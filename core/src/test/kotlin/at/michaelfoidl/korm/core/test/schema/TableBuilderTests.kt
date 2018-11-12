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
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer

class TableBuilderTests {

    private val referencedPrimaryKey = PrimaryKey("primaryKeyColumn", true)
    private val referencedTable = Table("entityToBeReferenced", emptyList(), this.referencedPrimaryKey, emptyList())

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

    private val primaryKeyBuilder: PrimaryKeyBuilder = mock()
    private val foreignKeyColumnBuilder: ForeignKeyBuilder = mock()
    private val foreignKeyColumnBuilderWithUnknownReference: ForeignKeyBuilder = mock()
    private val defaultColumnBuilder: ColumnBuilder = mock()

    @BeforeEach
    fun setup() {
        When calling this.primaryKeyBuilder.asPrimaryKeyBuilder() itAnswers Answer<PrimaryKeyBuilder> {
            val result: PrimaryKeyBuilder = mock()

            When calling result.toPrimaryKey() itReturns mock()

            result
        }

        When calling this.foreignKeyColumnBuilder.asForeignKeyBuilder() itAnswers Answer<ForeignKeyBuilder> {
            val result: ForeignKeyBuilder = mock()

            When calling result.getReferencedTable(any()) itReturns this.referencedTable
            When calling result.getReferencedColumn(any()) itReturns this.referencedPrimaryKey
            When calling result.toForeignKey(any(), any()) itReturns mock()

            result
        }

        When calling this.foreignKeyColumnBuilderWithUnknownReference.asForeignKeyBuilder() itAnswers Answer<ForeignKeyBuilder> {
            val result: ForeignKeyBuilder = mock()

            When calling result.getReferencedTable(any()) itReturns null

            result
        }
    }

    @Test
    fun tableBuilder_canBeResolved_shouldReturnTrueIfEverythingIsOK() {

        // Arrange
        val builder = object : TableBuilder() {
            override fun getName(): String {
                return ""
            }

            override val columnBuilders: Collection<ColumnBuilder>
                get() = listOf(this@TableBuilderTests.defaultColumnBuilder)

            override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
                get() = listOf(this@TableBuilderTests.foreignKeyColumnBuilder)

            override val primaryKeyBuilder: PrimaryKeyBuilder
                get() = this@TableBuilderTests.primaryKeyBuilder
        }

        // Act
        val result = builder.canBeResolved(provideTables())

        // Assert
        result shouldBe true
    }

    @Test
    fun tableBuilder_canBeResolved_shouldReturnFalseIfAnyForeignKeyReferencesAnUnknownTable() {

        // Arrange
        val builder = object : TableBuilder() {
            override fun getName(): String {
                return ""
            }

            override val columnBuilders: Collection<ColumnBuilder>
                get() = listOf(this@TableBuilderTests.defaultColumnBuilder)

            override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
                get() = listOf(this@TableBuilderTests.foreignKeyColumnBuilderWithUnknownReference)

            override val primaryKeyBuilder: PrimaryKeyBuilder
                get() = this@TableBuilderTests.primaryKeyBuilder
        }

        // Act
        val result = builder.canBeResolved(provideTables())

        // Assert
        result shouldBe false
    }

    @Test
    fun tableBuilder_canBeResolved_shouldAlwaysReturnTrueForEntityWithoutForeignKeys() {

        // Arrange
        val builder = object : TableBuilder() {
            override fun getName(): String {
                return ""
            }

            override val columnBuilders: Collection<ColumnBuilder>
                get() = listOf(this@TableBuilderTests.defaultColumnBuilder)

            override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
                get() = emptyList()

            override val primaryKeyBuilder: PrimaryKeyBuilder
                get() = this@TableBuilderTests.primaryKeyBuilder
        }

        // Act
        val result = builder.canBeResolved(emptyList())

        // Assert
        result shouldBe true
    }

    @Test
    fun tableBuilder_toTable_shouldReturnCorrespondingTable() {

        // Arrange
        val builder = object : TableBuilder() {
            override fun getName(): String {
                return "entity"
            }

            override val columnBuilders: Collection<ColumnBuilder>
                get() = listOf(this@TableBuilderTests.defaultColumnBuilder)

            override val foreignKeyBuilders: Collection<ForeignKeyBuilder>
                get() = listOf(this@TableBuilderTests.foreignKeyColumnBuilder)

            override val primaryKeyBuilder: PrimaryKeyBuilder
                get() = this@TableBuilderTests.primaryKeyBuilder
        }

        // Act
        val result = builder.toTable(provideTables(), provideColumns())

        // Assert
        result shouldNotBe null
        result.name shouldEqual "entity"
        result.columns.count() shouldEqual 1
        result.foreignKeys.count() shouldEqual 1
        result.primaryKey shouldNotBe null
    }
}