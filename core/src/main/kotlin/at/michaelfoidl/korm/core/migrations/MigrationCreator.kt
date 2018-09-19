package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import org.joda.time.DateTime
import java.io.File

class MigrationCreator(
        private val configuration: KormConfiguration
) {
    fun createMigration(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): String {
        val migrationName: String = "Migration" + DateTime.now().toString("yyyyMMddhhmmss")

        val file = FileSpec.builder(this.configuration.migrationPackage, migrationName)
                .addType(
                        TypeSpec.classBuilder(migrationName)
                                .build()
                )

        file.build().writeTo(File(this.configuration.rootDir, ""))
        return migrationName
    }
}