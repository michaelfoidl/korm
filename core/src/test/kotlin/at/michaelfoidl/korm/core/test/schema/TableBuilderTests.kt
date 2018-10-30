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
import at.michaelfoidl.korm.core.testUtils.schema.entities.Entity
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityWithAnnotatedProperties
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityWithBlankDefinedName
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityWithoutDefinedName
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityThatCannotBeResolved
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test

class TableBuilderTests {

    private val referencedPrimaryKey = PrimaryKey("primaryKeyColumn", true)
    private val referencedTable = Table("myEntity", listOf(this.referencedPrimaryKey), this.referencedPrimaryKey, emptyList())

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
    fun tableBuilder_nameOfTableWithDefinedName_shouldUseDefinedValue() {

        // Arrange
        val builder = TableBuilder(EntityWithAnnotatedProperties::class)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "myEntity"
    }

    @Test
    fun tableBuilder_nameOfTableWithBlankDefinedName_shouldEntityClassName() {

        // Arrange
        val builder = TableBuilder(EntityWithBlankDefinedName::class)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "entityWithBlankDefinedName"
    }

    @Test
    fun tableBuilder_nameOfTableWithoutDefinedName_shouldEntityClassName() {

        // Arrange
        val builder = TableBuilder(EntityWithoutDefinedName::class)

        // Act
        val result = builder.getName()

        // Assert
        result shouldEqual "entityWithoutDefinedName"
    }

    @Test
    fun tableBuilder_canBeResolved_shouldReturnTrueIfEverythingIsOK() {

        // Arrange
        val builder = TableBuilder(Entity::class)

        // Act
        val result = builder.canBeResolved(provideTables())

        // Assert
        result shouldBe true
    }

    @Test
    fun tableBuilder_canBeResolved_shouldReturnFalseIfAnyForeignKeyReferencesAnUnknownTable() {

        // Arrange
        val builder = TableBuilder(EntityThatCannotBeResolved::class)

        // Act
        val result = builder.canBeResolved(provideTables())

        // Assert
        result shouldBe false
    }

    @Test
    fun tableBuilder_canBeResolved_shouldAlwaysReturnTrueForEntityWithoutForeignKeys() {

        // Arrange
        val builder = TableBuilder(EntityWithBlankDefinedName::class)

        // Act
        val result = builder.canBeResolved(emptyList())

        // Assert
        result shouldBe true
    }

    @Test
    fun tableBuilder_toTable_shouldReturnCorrespondingTable() {

        // Arrange
        val builder = TableBuilder(Entity::class)

        // Act
        val result = builder.toTable(provideTables(), provideColumns())

        // Assert
        result shouldNotBe null
        result.name shouldEqual "entity"
        result.columns.count() shouldEqual 4
        result.foreignKeys.count() shouldEqual 1
        result.primaryKey shouldNotBe null
        result.columns.contains(result.foreignKeys.first()) shouldBe true
        result.columns.contains(result.primaryKey) shouldBe true
    }
}