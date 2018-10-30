package at.michaelfoidl.korm.core.test.database

import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.testUtils.database.DatabaseInterface
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.testUtils.BuildProcessFaker
import at.michaelfoidl.korm.types.ClassTypeWrapper
import org.amshove.kluent.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer
import java.io.File

class DatabaseCreatorTests {

    private val ioOracle: IOOracle = mock()

    private val databaseConfiguration: DatabaseConfiguration = mock()

    private val kormConfiguration: KormConfiguration = mock()

    private fun compileDatabase(fileName: String): Boolean {
        return BuildProcessFaker.compileDatabase(
                fileName,
                "build/tmp/test",
                "",
                "build/tmp/test")
    }

    private inline fun <reified T : Database> compileAndLoadDatabase(fileName: String): T? {
        return BuildProcessFaker.compileAndLoadDatabase(
                fileName,
                "build/tmp/test",
                "",
                "build/tmp/test")
    }

    @BeforeEach
    fun setup() {
        When calling this.ioOracle.getDatabaseBuilder(any(), any()) itAnswers Answer<IOBuilder> {
            val builder: IOBuilder = mock()
            When calling builder.simpleName() itReturns "MyDatabase"
            When calling builder.packageName() itReturns ""
            When calling builder.sourcePath(any()) itReturns "build/tmp/test"
            builder
        }
    }

    @Test
    fun databaseCreator_createDatabase_shouldGenerateNewFile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)

        // Act
        val result = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Assert
        File("build/tmp/test/$result.kt").exists() shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldCompile() {

        // Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Act
        val result = compileDatabase(sourceFileName)

        // Assert
        result shouldBe true
    }

    @Test
    fun databaseCreator_createdDatabase_shouldExtendDatabaseInterfaceClass() {

        /// Arrange
        val databaseCreator = DatabaseCreator(this.databaseConfiguration, this.kormConfiguration, this.ioOracle)
        val sourceFileName = databaseCreator.createDatabase(ClassTypeWrapper(DatabaseInterface::class))

        // Act
        val result = compileAndLoadDatabase<DatabaseInterface>(sourceFileName)

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf DatabaseInterface::class
    }
}