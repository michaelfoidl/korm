package at.michaelfoidl.korm.core.test.migrations

import at.michaelfoidl.korm.core.migrations.DatabaseSchemaAnalyzer
import at.michaelfoidl.korm.core.schema.DatabaseSchema
import at.michaelfoidl.korm.core.schema.DatabaseType
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity1
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity1WithAdditionalColumn
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity2
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity2WithAdditionalColumn
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity2WithDifferentDatatype
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity3
import at.michaelfoidl.korm.core.testUtils.migrations.entities.Entity3WithAdditionalColumn
import com.beust.klaxon.Klaxon
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class DatabaseSchemaAnalyzerTests {

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoMissingTable() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoMissingColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoDroppedTable() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedTables(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoDroppedColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoChangedColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_changeDetection_shouldFindMissingTable() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().name shouldEqual "entity2"
    }

    @Test
    fun schemaAnalyzer_missingColumn_shouldFindMissingColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2WithAdditionalColumn::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().name shouldEqual "integer"
    }

    @Test
    fun schemaAnalyzer_missingColumnsAcrossTables_shouldFindAllMissingColumns() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1WithAdditionalColumn::class, Entity2WithAdditionalColumn::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 2
        result.map { it.name } shouldContain "thirdProperty"
        result.map { it.name } shouldContain "integer"
    }

    @Test
    fun schemaAnalyzer_droppedTable_shouldFindDroppedTable() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedTables(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().name shouldEqual "entity2"
    }

    @Test
    fun schemaAnalyzer_droppedColumn_shouldFindDroppedColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2WithAdditionalColumn::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().name shouldEqual "integer"
    }

    @Test
    fun schemaAnalyzer_droppedColumnsAcrossTables_shouldFindAllDroppedColumns() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1WithAdditionalColumn::class, Entity2WithAdditionalColumn::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 2
        result.map { it.name } shouldContain "thirdProperty"
        result.map { it.name } shouldContain "integer"
    }

//    @Test
//    fun schemaAnalyzer_changedColumnWithDifferentVarcharLength_shouldFindChangedColumn() {
//
//        // Arrange
//        val table1 = object : Table("table1") {
//            val id = long("id").primaryKey()
//            val data = varchar("data", 255)
//        }
//        val table2 = object : Table("table2") {
//            val id = long("id").primaryKey()
//            val data = varchar("data", 255)
//        }
//        val table2WithChangedColumn = object : Table("table2") {
//            val id = long("id").primaryKey()
//            val data = varchar("data", 256)
//        }
//
//        val currentSchema = DatabaseSchema(
//                listOf(table1, table2)
//        )
//
//        val targetSchema = DatabaseSchema(
//                listOf(table1, table2WithChangedColumn)
//        )
//
//        // Act
//        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)
//
//        // Assert
//        result.size shouldEqual 1
//        result.first().first shouldEqual table2.data
//        result.first().second shouldEqual table2WithChangedColumn.data
//    }

    @Test
    fun schemaAnalyzer_changedColumnWithDifferentDataType_shouldFindChangedColumn() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2WithAdditionalColumn::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2WithDifferentDatatype::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().first.name shouldEqual "integer"
        result.first().first.dataType shouldEqual DatabaseType.Integer
        result.first().second.name shouldEqual "integer"
        result.first().second.dataType shouldEqual DatabaseType.Long
    }

    @Test
    fun schemaAnalyzer_multipleChanges_shouldFindChangedColumnBetweenDroppedOrMissingColumns() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1WithAdditionalColumn::class, Entity2WithAdditionalColumn::class, Entity3::class)
        )

        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(Entity1::class, Entity2WithDifferentDatatype::class, Entity3WithAdditionalColumn::class)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().first.name shouldEqual "integer"
        result.first().first.dataType shouldEqual DatabaseType.Integer
        result.first().second.name shouldEqual "integer"
        result.first().second.dataType shouldEqual DatabaseType.Long
    }
}