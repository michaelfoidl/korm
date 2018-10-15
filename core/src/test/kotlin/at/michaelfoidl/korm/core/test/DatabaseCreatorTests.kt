package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.configuration.DefaultDatabaseConfiguration
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.testUtils.DatabaseInterface
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import at.michaelfoidl.korm.types.ClassTypeWrapper
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import java.io.File

class DatabaseCreatorTests {

    private val databaseConfiguration: DatabaseConfiguration = DefaultDatabaseConfiguration(
            databaseVersion = 1
    )

    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
            rootPackage = "at.michaelfoidl.korm.core.test.generated",
            sourceDirectory = "build/tmp/test/src",
            buildDirectory = "build/tmp/test/build"
    )

    private fun compileDatabase(fileName: String): Boolean {
        return BuildProcessFaker.compileDatabase(
                fileName,
                this.kormConfiguration.sourceDirectory,
                IOOracle.getDatabasePackage(this.kormConfiguration),
                this.kormConfiguration.buildDirectory)
    }

    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
        return BuildProcessFaker.compileAndLoadDatabase(
                fileName,
                this.kormConfiguration.sourceDirectory,
                IOOracle.getDatabasePackage(this.kormConfiguration),
                this.kormConfiguration.buildDirectory)
    }

    @Test
    fun databaseCreator_createDatabase_shouldGenerateNewFile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration)

        // Act
        val result = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Assert
        File(IOOracle.getDatabaseFolderPath(this.kormConfiguration) + "/$result.kt").exists() shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldCompile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration)
        val sourceFileName = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Act
        val result = compileDatabase(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldExtendDatabaseInterfaceClass() {

        /// Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration)
        val sourceFileName = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Act
        val result = compileAndLoadDatabase<DatabaseInterface>(sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf DatabaseInterface::class
    }
}