package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.TypeWrapper
import at.michaelfoidl.korm.types.asTypeName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File

class DatabaseCreator internal constructor(
        private val databaseConfiguration: DatabaseConfiguration,
        private val kormConfiguration: KormConfiguration,
        private val ioOracle: IOOracle = IOOracle
) {
    constructor(
            databaseConfiguration: DatabaseConfiguration,
            kormConfiguration: KormConfiguration
    ) : this(databaseConfiguration, kormConfiguration, IOOracle)

    fun createDatabase(element: TypeWrapper): String {
        val databaseBuilder = this.ioOracle.getDatabaseBuilder(this.databaseConfiguration, this.kormConfiguration)
        val databaseName = databaseBuilder.simpleName()
        FileSpec.builder(databaseBuilder.packageName(), databaseName)
                .addType(
                        TypeSpec.classBuilder(databaseName)
                                .superclass(element.asTypeName())
                                .build()
                )
                .build()
                .writeTo(File(databaseBuilder.sourcePath(true), ""))
        return databaseName
    }
}