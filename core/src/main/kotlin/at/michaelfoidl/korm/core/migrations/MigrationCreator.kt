package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import com.squareup.kotlinpoet.*
import org.joda.time.DateTime
import java.io.File

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
                                .addFunction(
                                        FunSpec.builder("up")
                                                .addModifiers(KModifier.OVERRIDE)
                                                .addParameter(
                                                        ParameterSpec.builder("connection", DatabaseConnection::class).build()
                                                )
                                                .build()
                                )
                                .addFunction(
                                        FunSpec.builder("down")
                                                .addModifiers(KModifier.OVERRIDE)
                                                .addParameter(
                                                        ParameterSpec.builder("connection", DatabaseConnection::class).build()
                                                )
                                                .build()
                                )
                                .build()
                )

        file.build().writeTo(File(this.configuration.rootDir, ""))
        return migrationName
    }
}