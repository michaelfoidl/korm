package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseType
import at.michaelfoidl.korm.interfaces.KormConfiguration
import kotlin.reflect.KClass

class DefaultKormConfiguration(
        override val databaseType: DatabaseType,
        override val databaseVersion: Long,
        override val databaseInterface: KClass<out Database>,
        override val databaseName: String = "Database",
        override val databasePath: String? = null,
        override val username: String = "",
        override val password: String = "",
        override val migrationPackage: String = "migrations",
        override val databasePackage: String = "database",
        override val rootPackage: String,
        override val rootDirectory: String = "src/main"
) : KormConfiguration