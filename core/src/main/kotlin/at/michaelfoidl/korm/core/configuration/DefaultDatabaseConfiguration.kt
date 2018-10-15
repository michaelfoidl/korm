package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.interfaces.DatabaseConfiguration

class DefaultDatabaseConfiguration(
        override val databaseName: String = "Database",
        override val databaseVersion: Long,
        override val databasePath: String? = null,
        override val username: String = "",
        override val password: String = ""
) : DatabaseConfiguration