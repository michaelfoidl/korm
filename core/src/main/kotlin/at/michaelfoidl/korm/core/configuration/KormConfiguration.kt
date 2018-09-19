package at.michaelfoidl.korm.core.configuration

class KormConfiguration(
        val databaseType: DatabaseType = DatabaseType.SQLite,
        val databaseVersion: Long = 1,
        val username: String = "",
        val password: String = "",
        val migrationPackage: String = "migrations",
        val rootDir: String = ""
)