package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.testUtils.DatabaseInterface
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.testUtils.ClassLoader
import at.michaelfoidl.korm.testUtils.Compiler
import at.michaelfoidl.korm.testUtils.PackageDirectoryConverter
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class DatabaseCreatorTests {

    private val configuration: KormConfiguration = DefaultKormConfiguration(
            DatabaseType.SQLite,
            1,
            DatabaseInterface::class,
            "",
            "",
            "",
            "at.michaelfoidl.korm.core.test.generated.migrations",
            "at.michaelfoidl.korm.core.test.generated.database",
            "at.michaelfoidl.korm.core.test",
            "build/tmp/test/src"
    )

    private fun getDatabasePath(): String {
        return listOf(
                Paths.get("").toAbsolutePath().toString(),
                this.configuration.rootDirectory,
                PackageDirectoryConverter.convertPackageToDirectoryStructure(this.configuration.databasePackage),
                "Database.kt"
        ).joinToString("/")
    }

    private fun compileDatabase(buildFolderPath: String): Boolean {
        val sourceFilePath = getDatabasePath()
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    private inline fun <reified T : Database> loadDatabase(buildFolderPath: String): T {
        return ClassLoader(File(buildFolderPath)).createInstance<T>(this.configuration.databasePackage + ".Database")!!
    }

    @Test
    fun databaseCreator_createDatabase_shouldGenerateNewFile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.configuration)

        // Act
        databaseCreator.createDatabase()
        val path = getDatabasePath()

        // Assert
        File(path).exists() shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldCompile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.configuration)
        databaseCreator.createDatabase()

        // Act
        val result = compileDatabase(Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDirectory + "/../build")

        // Assert
        result shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldExtendDatabaseInterfaceClass() {

        /// Arrange
        val databaseCreator = DatabaseCreator(this.configuration)
        databaseCreator.createDatabase()
        val buildFolderPath = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDirectory + "/../build"
        compileDatabase(buildFolderPath)

        // Act
        val result = loadDatabase<DatabaseInterface>(buildFolderPath)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf DatabaseInterface::class
    }
}