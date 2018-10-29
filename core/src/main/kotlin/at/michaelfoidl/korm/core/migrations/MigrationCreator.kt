package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.core.schema.DatabaseSchema
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.squareup.kotlinpoet.*
import java.io.File

class MigrationCreator internal constructor(
        private val databaseConfiguration: DatabaseConfiguration,
        private val kormConfiguration: KormConfiguration,
        private val ioOracle: IOOracle = IOOracle
) {
    constructor(
            databaseConfiguration: DatabaseConfiguration,
            kormConfiguration: KormConfiguration
    ) : this(databaseConfiguration, kormConfiguration, IOOracle)


    fun createMigration(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): String {
        val migrationBuilder: IOBuilder = this.ioOracle.getMigrationBuilder(this.databaseConfiguration, this.kormConfiguration)
        val migrationName = migrationBuilder.simpleName()
        FileSpec.builder(migrationBuilder.packageName(), migrationName)
                .addType(
                        TypeSpec.classBuilder(migrationName)
                                .superclass(BaseMigration::class)
                                .addSuperclassConstructorParameter("1")
                                .addFunction(createUpTransformation(currentSchema, targetSchema))
                                .addFunction(createDownTransformation(currentSchema, targetSchema))
                                .build()
                )
                .run {
                    val packageName = this@MigrationCreator.ioOracle.getTableBuilder(this@MigrationCreator.kormConfiguration).packageName()
                    currentSchema.tables
                            .union(targetSchema.tables)
                            .distinctBy { it.name }
                            .forEach {
                                this.addImport(packageName, this@MigrationCreator.ioOracle.getTableName(it.name.capitalize()))
                            }
                    this
                }
                .build()
                .writeTo(File(migrationBuilder.sourcePath(true), ""))
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
                        this.addCode("createTable(${this@MigrationCreator.ioOracle.getTableName(table.name.capitalize())})")
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