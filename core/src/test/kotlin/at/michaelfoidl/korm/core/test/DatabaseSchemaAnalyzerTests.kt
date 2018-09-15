package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.migrations.DatabaseSchemaAnalyzer
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.jetbrains.exposed.sql.Table
import org.junit.jupiter.api.Test

class DatabaseSchemaAnalyzerTests {

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoMissingTable() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoMissingColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoDroppedTable() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedTables(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoDroppedColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_noChanges_shouldFindNoChangedColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.isEmpty() shouldBe true
    }

    @Test
    fun schemaAnalyzer_changeDetection_shouldFindMissingTable() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val missingTable = object : Table("missingTable") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, missingTable)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result shouldContain missingTable
    }

    @Test
    fun schemaAnalyzer_missingColumn_shouldFindMissingColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
        }
        val table2WithMissingColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, table2WithMissingColumn)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result shouldContain table2WithMissingColumn.data
    }

    @Test
    fun schemaAnalyzer_missingColumnsAcrossTables_shouldFindAllMissingColumns() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
        }
        val table1WithMissingColumn = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2WithMissingColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1WithMissingColumn, table2WithMissingColumn)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getMissingColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 2
        result shouldContain table1WithMissingColumn.data
        result shouldContain table2WithMissingColumn.data
    }

    @Test
    fun schemaAnalyzer_droppedTable_shouldFindDroppedTable() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val droppedTable = object : Table("missingTable") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, droppedTable)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedTables(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result shouldContain droppedTable
    }

    @Test
    fun schemaAnalyzer_droppedColumn_shouldFindDroppedColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
        }
        val table2WithDroppedColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, table2WithDroppedColumn)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result shouldContain table2WithDroppedColumn.data
    }

    @Test
    fun schemaAnalyzer_droppedColumnsAcrossTables_shouldFindAllDroppedColumns() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
        }
        val table1WithDroppedColumn = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2WithDroppedColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1WithDroppedColumn, table2WithDroppedColumn)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getDroppedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 2
        result shouldContain table1WithDroppedColumn.data
        result shouldContain table2WithDroppedColumn.data
    }

    @Test
    fun schemaAnalyzer_changedColumnWithDifferentVarcharLength_shouldFindChangedColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2WithChangedColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 256)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, table2WithChangedColumn)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().first shouldEqual table2.data
        result.first().second shouldEqual table2WithChangedColumn.data
    }

    @Test
    fun schemaAnalyzer_changedColumnWithDifferentDataType_shouldFindChangedColumn() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2 = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val table2WithChangedColumn = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = text("data")
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, table2)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, table2WithChangedColumn)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().first shouldEqual table2.data
        result.first().second shouldEqual table2WithChangedColumn.data
    }

    @Test
    fun schemaAnalyzer_multipleChanges_shouldFindChangedColumnBetweenDroppedOrMissingColumns() {

        // Arrange
        val table1 = object : Table("table1") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
        }
        val currentTable2 = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 255)
            val dropped = varchar("dropped", 255)
        }
        val targetTable2 = object : Table("table2") {
            val id = long("id").primaryKey()
            val data = varchar("data", 256)
            val missing = varchar("missing", 255)
        }

        val currentSchema = DatabaseSchema(
                listOf(table1, currentTable2)
        )

        val targetSchema = DatabaseSchema(
                listOf(table1, targetTable2)
        )

        // Act
        val result = DatabaseSchemaAnalyzer.getChangedColumns(currentSchema, targetSchema)

        // Assert
        result.size shouldEqual 1
        result.first().first shouldEqual currentTable2.data
        result.first().second shouldEqual targetTable2.data
    }
}