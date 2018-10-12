package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.migrations.BaseMigration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.core.testUtils.*
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.interfaces.Migration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class MigrationCreatorTests {

    private val configuration: KormConfiguration = DefaultKormConfiguration(
            databaseType = DatabaseType.SQLite,
            databaseVersion = 1,
            databaseInterface = DatabaseInterface::class,
            migrationPackage = "at.michaelfoidl.korm.core.test.generated.migrations",
            databasePackage = "at.michaelfoidl.korm.core.test.generated.database",
            rootPackage = "at.michaelfoidl.korm.core.test"
//            rootDirectory = "build/tmp/test/src"
    )

    private fun compileMigration(fileName: String): Boolean {
        return BuildProcessFaker.compileMigration(
                fileName,
                this.configuration.rootDirectory,
                this.configuration.migrationPackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configuration.rootDirectory + "/../../build/korm")
    }

    private fun compileAndLoadMigration(fileName: String): Migration? {
        return BuildProcessFaker.compileAndLoadMigration(
                fileName,
                this.configuration.rootDirectory,
                this.configuration.migrationPackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configuration.rootDirectory + "/../../build/korm")
    }

    @Test
    fun migrationCreator_createMigration_shouldGenerateNewFile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)

        // Act
        val result = migrationCreator.createMigration(currentSchema, targetSchema)

        // Assert
        File(IOOracle.getMigrationFolderPath(this.configuration) + "/$result.kt").exists() shouldBe true
    }

    @Test
    fun migrationCreator_createdMigration_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigration_shouldExtendBaseMigration() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileAndLoadMigration(sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf BaseMigration::class
    }

    @Test
    fun migrationCreator_createdMigrationWithMissingTable_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table, MissingTable)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithMissingColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(TableWithMissingColumn)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithDroppedTable_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table, DroppedTable)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithDroppedColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(TableWithDroppedColumn)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithChangedColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(TableWithChangedColumn)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }
}