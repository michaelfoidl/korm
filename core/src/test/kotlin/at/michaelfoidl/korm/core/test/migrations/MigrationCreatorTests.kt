package at.michaelfoidl.korm.core.test.migrations

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.migrations.BaseMigration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.core.schema.DatabaseSchema
import at.michaelfoidl.korm.core.testUtils.entities.*
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.interfaces.Migration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer
import java.io.File

class MigrationCreatorTests {

    private val ioOracle: IOOracle = mock()
    private val databaseConfiguration: DatabaseConfiguration = mock()
    private val kormConfiguration: KormConfiguration = mock()

    private fun compileMigration(fileName: String): Boolean {
        return BuildProcessFaker.compileMigration(
                fileName,
                "build/tmp/test",
                "",
                "build/tmp/test")
    }

    private fun compileAndLoadMigration(fileName: String): Migration? {
        return BuildProcessFaker.compileAndLoadMigration(
                fileName,
                "build/tmp/test",
                "",
                "build/tmp/test")
    }

    @BeforeEach
    fun setup() {
        When calling this.ioOracle.getTableName(any<String>()) itAnswers Answer<String> {
            it.arguments.first().toString() + "Table"
        }

        When calling this.ioOracle.getTableBuilder(any()) itAnswers Answer<IOBuilder> {
            val builder: IOBuilder = mock()
            When calling builder.packageName() itReturns "at.michaelfoidl.korm.core.testUtils.tables"
            builder
        }

        When calling this.ioOracle.getMigrationBuilder(any(), any()) itAnswers Answer<IOBuilder> {
            val builder: IOBuilder = mock()
            When calling builder.simpleName() itReturns "MyMigration"
            When calling builder.packageName() itReturns ""
            When calling builder.sourcePath(any()) itReturns "build/tmp/test"
            builder
        }
    }

    @Test
    fun migrationCreator_createMigration_shouldGenerateNewFile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)

        // Act
        val result = migrationCreator.createMigration(currentSchema, targetSchema)

        // Assert
        File("build/tmp/test/$result.kt").exists() shouldBe true
    }

    @Test
    fun migrationCreator_createdMigration_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigration_shouldExtendBaseMigration() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
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
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithMissingColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1WithAdditionalColumn::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithDroppedTable_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithDroppedColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1WithAdditionalColumn::class, SimpleEntity2::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigrationWithChangedColumn_shouldCompile() {

        // Arrange
        val currentSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2WithAdditionalColumn::class)
        )
        val targetSchema = DatabaseSchema.fromEntityCollection(
                listOf(SimpleEntity1::class, SimpleEntity2WithDifferentDatatype::class)
        )
        val migrationCreator = MigrationCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)

        // Act
        val result = compileMigration(sourceFileName)

        // Assert
        result shouldBe true
    }
}