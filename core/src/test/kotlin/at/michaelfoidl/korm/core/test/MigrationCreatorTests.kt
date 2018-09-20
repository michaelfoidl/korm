package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.DatabaseType
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.migrations.Migration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import at.michaelfoidl.korm.core.testUtils.ClassLoader
import at.michaelfoidl.korm.core.testUtils.Compiler
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.jetbrains.exposed.sql.Table
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

    private inline fun <reified T: Migration> loadMigration(buildFolderPath: String, fileName: String): T {
        return ClassLoader(File(buildFolderPath)).createInstance<T>(this.configuration.migrationPackage + "." + fileName)!!
    }

    @Test
    fun migrationCreator_createMigration_shouldGenerateNewFile() {

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
}