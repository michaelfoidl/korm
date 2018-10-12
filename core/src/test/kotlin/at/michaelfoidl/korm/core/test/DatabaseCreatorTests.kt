package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.testUtils.DatabaseInterface
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Paths

class DatabaseCreatorTests {

    private val configuration: KormConfiguration = DefaultKormConfiguration(
            databaseType = DatabaseType.SQLite,
            databaseVersion = 1,
            databaseInterface = DatabaseInterface::class,
            migrationPackage = "at.michaelfoidl.korm.core.test.generated.migrations",
            databasePackage = "at.michaelfoidl.korm.core.test.generated.database",
            rootPackage = "at.michaelfoidl.korm.core.test"
//            rootDirectory = "build/tmp/test/src"
    )

    private fun compileDatabase(fileName: String): Boolean {
        return BuildProcessFaker.compileDatabase(
                fileName,
                this.configuration.rootDirectory,
                this.configuration.databasePackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configuration.rootDirectory + "/../../build/korm")
    }

    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
        return BuildProcessFaker.compileAndLoadDatabase(
                fileName,
                this.configuration.rootDirectory,
                this.configuration.databasePackage,
                Paths.get("").toAbsolutePath().toString() + "/" + this.configuration.rootDirectory + "/../../build/korm")
    }

    @Test
    fun databaseCreator_createDatabase_shouldGenerateNewFile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.configuration)

        // Act
        val result = databaseCreator.createDatabase()

        // Assert
        File(IOOracle.getDatabaseFolderPath(this.configuration) + "/$result.kt").exists() shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldCompile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.configuration)
        val sourceFileName = databaseCreator.createDatabase()

        // Act
        val result = compileDatabase(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldExtendDatabaseInterfaceClass() {

        /// Arrange
        val databaseCreator = DatabaseCreator(this.configuration)
        val sourceFileName = databaseCreator.createDatabase()

        // Act
        val result = compileAndLoadDatabase<DatabaseInterface>(sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf DatabaseInterface::class
    }
}