package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.DatabaseType
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.migrations.Migration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.core.testUtils.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class MigrationCreatorTests {

    private val configuration: KormConfiguration = KormConfiguration(
            DatabaseType.SQLite,
            1,
            "",
            "",
            "at.michaelfoidl.korm.integrationTests.migrations",
            "build/tmp/test/src"
    )

    private fun getMigrationPath(fileName: String): String {
        return listOf(
                Paths.get("").toAbsolutePath().toString(),
                this.configuration.rootDir,
                this.configuration.migrationPackage.replace('.', '/'),
                "$fileName.kt"
        ).joinToString("/")
    }

    private fun compileMigration(buildFolderPath: String, fileName: String): Boolean {
        val sourceFilePath = getMigrationPath(fileName)
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    private inline fun <reified T : Migration> loadMigration(buildFolderPath: String, fileName: String): T {
        return ClassLoader(File(buildFolderPath)).createInstance<T>(this.configuration.migrationPackage + "." + fileName)!!
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
        val path = getMigrationPath(result)

        // Assert
        File(path).exists() shouldBe true
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
        val result = compileMigration(Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build", sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun migrationCreator_createdMigration_shouldExtendMigration() {

        // Arrange
        val currentSchema = DatabaseSchema(
                listOf(Table)
        )

        val targetSchema = DatabaseSchema(
                listOf(Table)
        )

        val migrationCreator = MigrationCreator(this.configuration)
        val sourceFileName = migrationCreator.createMigration(currentSchema, targetSchema)
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"
        compileMigration(buildFolderPath, sourceFileName)

        // Act
        val result = loadMigration<Migration>(buildFolderPath, sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf Migration::class
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
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"

        // Act
        val result = compileMigration(buildFolderPath, sourceFileName)

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
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"

        // Act
        val result = compileMigration(buildFolderPath, sourceFileName)

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
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"

        // Act
        val result = compileMigration(buildFolderPath, sourceFileName)

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
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"

        // Act
        val result = compileMigration(buildFolderPath, sourceFileName)

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
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"

        // Act
        val result = compileMigration(buildFolderPath, sourceFileName)

        // Assert
        result shouldBe true
    }
}