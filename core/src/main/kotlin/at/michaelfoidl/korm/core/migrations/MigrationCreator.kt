package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.sqlite.SQLiteDatabase
import com.squareup.kotlinpoet.*
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.vendors.PostgreSQLDialect
import org.joda.time.DateTime
import java.io.File
import javax.sql.DataSource

class MigrationCreator(
        private val configuration: KormConfiguration
) {
    fun createMigration(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): String {
        val migrationName: String = "Migration" + DateTime.now().toString("yyyyMMddHHmmss")

        val file = FileSpec.builder(this.configuration.migrationPackage, migrationName)
                .addType(
                        TypeSpec.classBuilder(migrationName)
                                .superclass(Migration::class)
                                .addSuperclassConstructorParameter("1")
                                .addFunction(createUpTransformation(currentSchema, targetSchema))
                                .addFunction(createDownTransformation(currentSchema, targetSchema))
                                .build()
                )

        file.build().writeTo(File(this.configuration.rootDir, ""))
        return migrationName
    }

    private fun createUpTransformation(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): FunSpec {
        val function = FunSpec.builder("up")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(
                        ParameterSpec.builder("connection", DatabaseConnection::class).build()
                )
                .beginControlFlow("connection.executeInTransaction")


        DatabaseSchemaAnalyzer.getMissingTables(currentSchema, targetSchema).forEach { table ->
            function.addCode("createTable(%T)", table::class)
        }

        function.endControlFlow()

        return function.build()
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