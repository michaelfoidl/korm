package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.squareup.kotlinpoet.*
import java.io.File

class MigrationCreator(
        private val configuration: KormConfiguration
) {
    fun createMigration(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): String {
        val migrationName = createMigrationName()
        FileSpec.builder(this.configuration.migrationPackage, migrationName)
                .addType(
                        TypeSpec.classBuilder(migrationName)
                                .superclass(BaseMigration::class)
                                .addSuperclassConstructorParameter("1")
                                .addFunction(createUpTransformation(currentSchema, targetSchema))
                                .addFunction(createDownTransformation(currentSchema, targetSchema))
                                .build()
                )
                .build()
                .writeTo(File(this.configuration.rootDirectory, ""))
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

    private fun createMigrationName(): String {
        return "Migration_v" + this.configuration.databaseVersion + "_" + (this.configuration.databaseVersion + 1)
    }
}