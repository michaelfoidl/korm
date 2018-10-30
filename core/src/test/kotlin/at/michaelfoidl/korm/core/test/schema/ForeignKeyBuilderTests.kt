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

import at.michaelfoidl.korm.annotations.AutoIncrement
import at.michaelfoidl.korm.annotations.ColumnName
import at.michaelfoidl.korm.annotations.Indexed
import at.michaelfoidl.korm.core.schema.*
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityWithAnnotatedProperties
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaField

class ForeignKeyBuilderTests {

    private val referencedPrimaryKey = PrimaryKey("primaryKeyColumn", true)
    private val referencedTable = Table("entity", listOf(this.referencedPrimaryKey), this.referencedPrimaryKey, emptyList())

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
    fun foreignKeyBuilder_referencedTable_shouldFindCorrectTable() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.getReferencedTable(provideTables())

        // Assert
        result shouldNotBe null
        result shouldEqual this.referencedTable
    }

    @Test
    fun foreignKeyBuilder_referencedTable_shouldReturnNullForUnknownTable() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::invalidForeignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.getReferencedTable(provideTables())

        // Assert
        result shouldBe null
    }

    @Test
    fun foreignKeyBuilder_referencedColumn_shouldFindCorrectColumn() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.getReferencedColumn(provideColumns())

        // Assert
        result shouldNotBe null
        result shouldEqual this.referencedPrimaryKey
    }

    @Test
    fun foreignKeyBuilder_referencedColumn_shouldReturnNullForUnknownColumn() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::invalidForeignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.getReferencedColumn(provideColumns())

        // Assert
        result shouldBe null
    }

    @Test
    fun foreignKeyBuilder_toForeignKey_shouldReturnCorrespondingForeignKey() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.toForeignKey(provideTables(), provideColumns())

        // Assert
        result shouldNotBe null
        result.referencedTable shouldEqual this.referencedTable
        result.referencedColumn shouldEqual this.referencedPrimaryKey
    }

    @Test
    fun foreignKeyBuilder_toForeignKey_shouldFailWithUnknownReferencedTable() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::invalidForeignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val function = { builder.toForeignKey(provideTables(), provideColumns()) }

        // Assert
        function shouldThrow IllegalArgumentException::class
    }

    @Test
    fun foreignKeyBuilder_toForeignKey_shouldFailWithUnknownReferencedColumn() {

        // Arrange
        val builder = ForeignKeyBuilder(
                "foreignKeyColumn",
                Long::class,
                true,
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::invalidForeignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::foreignKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val function = { builder.toForeignKey(provideTables(), provideColumns()) }

        // Assert
        function shouldThrow IllegalArgumentException::class
    }
}