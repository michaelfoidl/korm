package at.michaelfoidl.korm.core.test

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.DatabaseType
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.migrations.Migration
import at.michaelfoidl.korm.core.migrations.MigrationCreator
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldNotBe
import org.jetbrains.exposed.sql.Table
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
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

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = "gitlab-ci")
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

        // Assert
        val path = Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/" + configuration.migrationPackage.replace('.', '/') + "/" + result + ".kt"
        File(path).exists() shouldBe true
        Compiler.execute(File(path), File(Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build")) shouldBe true
        ClassLoader(File(Paths.get("").toAbsolutePath().toString() + "/" + configuration.rootDir + "/../build"))
                .createInstance<Migration>(configuration.migrationPackage + "." + result) shouldNotBe null
    }
}