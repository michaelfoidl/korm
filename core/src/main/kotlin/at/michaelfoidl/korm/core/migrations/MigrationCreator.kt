package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.squareup.kotlinpoet.*
import java.io.File

class MigrationCreator(
        private val databaseConfiguration: DatabaseConfiguration,
        private val kormConfiguration: KormConfiguration
) {
    fun createMigration(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): String {
        val migrationName = IOOracle.getMigrationName(this.databaseConfiguration.databaseName, this.databaseConfiguration.databaseVersion)
        FileSpec.builder(IOOracle.getMigrationPackage(this.kormConfiguration), migrationName)
                .addType(
                        TypeSpec.classBuilder(migrationName)
                                .superclass(BaseMigration::class)
                                .addSuperclassConstructorParameter("1")
                                .addFunction(createUpTransformation(currentSchema, targetSchema))
                                .addFunction(createDownTransformation(currentSchema, targetSchema))
                                .build()
                )
                .build()
                .writeTo(File(IOOracle.getRootFolderPath(this.kormConfiguration), ""))
        return migrationName
    }

    private fun createUpTransformation(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): FunSpec {
        return FunSpec.builder("up")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(
                        ParameterSpec.builder("connection", DatabaseConnection::class).build()
                )
                .beginControlFlow("connection.executeInTransaction")
                .run {
                    DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema).forEach { table ->
                        this.addCode("createTable(%T)", table::class)
                    }
                    this
                }
                .endControlFlow()
                .build()
    }

    private fun createDownTransformation(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): FunSpec {
        return FunSpec.builder("down")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(
                        ParameterSpec.builder("connection", DatabaseConnection::class).build()
                )
                .build()
    }
}