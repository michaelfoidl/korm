package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.configuration.DefaultDatabaseConfiguration
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.TypeWrapper
import at.michaelfoidl.korm.types.asTypeName
import com.squareup.kotlinpoet.*
import java.io.File

class DatabaseCreator(
        private val databaseConfiguration: DatabaseConfiguration,
        private val kormConfiguration: KormConfiguration
) {
    fun createDatabase(element: TypeWrapper): String {
        val databaseBuilder = IOOracle.getDatabaseBuilder(this.databaseConfiguration.databaseName, this.databaseConfiguration.databaseVersion, this.kormConfiguration)
        val databaseName = databaseBuilder.simpleName()
        FileSpec.builder(databaseBuilder.packageName(), databaseName)
                .addType(
                        TypeSpec.classBuilder(databaseName)
                                .superclass(element.asTypeName())
//                                .addProperty(createDatabaseConfigurationProperty())
//                                .addProperty(createKormConfigurationProperty())
                                .build()
                )
                .build()
                .writeTo(File(databaseBuilder.sourcePath(true), ""))
        return databaseName
    }

    private fun createDatabaseConfigurationProperty(): PropertySpec {
        return PropertySpec
                .builder("databaseConfiguration", DatabaseConfiguration::class, KModifier.OVERRIDE)
                .getter(FunSpec
                        .getterBuilder()
                        .addCode("""
                            return %T(
                                    databaseName = %S,
                                    databaseVersion = %L,
                                    databasePath = %S,
                                    username = %S,
                                    password = %S
                            )
                        """.trimIndent(),
                                DefaultDatabaseConfiguration::class,
                                databaseConfiguration.databaseName,
                                databaseConfiguration.databaseVersion,
                                databaseConfiguration.databasePath ?: "",
                                databaseConfiguration.username,
                                databaseConfiguration.password)
                        .build())
                .build()
    }

    private fun createKormConfigurationProperty(): PropertySpec {
        return PropertySpec
                .builder("kormConfiguration", KormConfiguration::class, KModifier.OVERRIDE)
                .getter(FunSpec
                        .getterBuilder()
                        .addCode("""
                            return %T(
                                    migrationPackage = %S,
                                    kormPackage = %S,
                                    sourceDirectory = %S,
                                    buildDirectory = %S,
                                    rootDirectory = %S
                            )
                        """.trimIndent(),
                                DefaultKormConfiguration::class,
                                kormConfiguration.migrationPackage,
                                kormConfiguration.kormPackage,
                                kormConfiguration.sourceDirectory,
                                kormConfiguration.buildDirectory,
                                kormConfiguration.rootDirectory)
                        .build())
                .build()
    }
}