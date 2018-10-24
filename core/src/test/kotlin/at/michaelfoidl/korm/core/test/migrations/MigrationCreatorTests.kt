package at.michaelfoidl.korm.core.test.migrations

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.DefaultDatabaseConfiguration
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.migrations.BaseMigration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.core.testUtils.*
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.interfaces.Migration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File

class MigrationCreatorTests {

    private val databaseConfiguration: DatabaseConfiguration = DefaultDatabaseConfiguration(
            databaseName = "MyDatabase",
            databaseVersion = 1,
            databasePath = null,
            username = "",
            password = ""
    )

    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
            migrationPackage = "migrations",
            kormPackage = "at.michaelfoidl.korm.core.test.generated",
            sourceDirectory = "build/tmp/test/src",
            buildDirectory = "build/tmp/test/build",
            rootDirectory = ""
    )

    private val migrationBuilder: IOBuilder = IOOracle.getMigrationBuilder(
            this.databaseConfiguration,
            this.kormConfiguration)

    private fun compileMigration(fileName: String): Boolean {
        return BuildProcessFaker.compileMigration(
                fileName,
                this.migrationBuilder.sourcePath(true),
                this.migrationBuilder.packageName(),
                this.migrationBuilder.buildPath(true))
    }

    private fun compileAndLoadMigration(fileName: String): Migration? {
        return BuildProcessFaker.compileAndLoadMigration(
                fileName,
                this.migrationBuilder.sourcePath(true),
                this.migrationBuilder.packageName(),
                this.migrationBuilder.buildPath(true))
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)

        // Act
        val result = migrationCreator.createMigration(currentSchema, targetSchema)

        // Assert
        File("${this.migrationBuilder.sourcePath()}/$result.kt").exists() shouldBe true
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
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

        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }
}