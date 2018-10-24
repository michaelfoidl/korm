package at.michaelfoidl.korm.core.test.database

import at.michaelfoidl.korm.core.configuration.DefaultDatabaseConfiguration
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
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

    private val databaseBuilder: IOBuilder = IOOracle.getDatabaseBuilder(
            this.databaseConfiguration,
            this.kormConfiguration)

    private fun compileDatabase(fileName: String): Boolean {
        return BuildProcessFaker.compileDatabase(
                fileName,
                this.databaseBuilder.sourcePath(true),
                this.databaseBuilder.packageName(),
                this.databaseBuilder.buildPath(true))
    }

    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
        return BuildProcessFaker.compileAndLoadDatabase(
                fileName,
                this.databaseBuilder.sourcePath(true),
                this.databaseBuilder.packageName(),
                this.databaseBuilder.buildPath(true))
    }

    @Test
    fun databaseCreator_createDatabase_shouldGenerateNewFile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration)

        // Act
        val result = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Assert
        File("${this.databaseBuilder.sourcePath()}/$result.kt").exists() shouldBe true
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