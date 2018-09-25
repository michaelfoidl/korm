package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.core.database.Database
import kotlin.reflect.KClass

class KormConfiguration(
        val databaseType: DatabaseType = DatabaseType.SQLite,
        val databaseVersion: Long = 1,
        val databaseInterface: KClass<out Database>,
        val databasePath: String? = null,
        val username: String = "",
        val password: String = "",
        val migrationPackage: String = "migrations",
        val databasePackage: String = "database",
        val rootPackage: String = "",
        val rootDirectory: String = ""
)