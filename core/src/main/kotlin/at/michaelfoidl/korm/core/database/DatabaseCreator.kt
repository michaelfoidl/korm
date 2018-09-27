package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.squareup.kotlinpoet.*
import java.io.File

class DatabaseCreator(
        private val configuration: KormConfiguration
) {
    fun createDatabase() {
        FileSpec.builder(this.configuration.databasePackage, "Database")
                .addType(
                        TypeSpec.classBuilder("Database")
                                .superclass(configuration.databaseInterface)
                                .addProperty(createConfigurationProperty())
                                .build()
                )
                .build()
                .writeTo(File(this.configuration.rootDirectory, ""))
    }

    private fun createConfigurationProperty(): PropertySpec {
        return PropertySpec
                .builder("configuration", KormConfiguration::class, KModifier.OVERRIDE)
                .getter(FunSpec
                        .getterBuilder()
                        .addCode("""
                            return %T(
                                    databaseType = %T.%L,
                                    databaseVersion = %L,
                                    databaseInterface = %T::class,
                                    databasePath = %S,
                                    username = %S,
                                    password = %S,
                                    migrationPackage = %S,
                                    databasePackage = %S,
                                    rootPackage = %S,
                                    rootDirectory = %S
                            )
                        """.trimIndent(),
                                DefaultKormConfiguration::class,
                                DatabaseType::class,
                                configuration.databaseType,
                                configuration.databaseVersion,
                                configuration.databaseInterface,
                                configuration.databasePath ?: "",
                                configuration.username,
                                configuration.password,
                                configuration.migrationPackage,
                                configuration.databasePackage,
                                configuration.rootPackage,
                                configuration.rootDirectory)
                        .build())
                .build()
    }
}