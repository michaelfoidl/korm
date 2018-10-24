package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import java.util.*

class DefaultDatabaseConfiguration(
        override val databaseName: String = "Database",
        override val databaseVersion: Long = 1,
        override val databasePath: String? = null,
        override val username: String = "",
        override val password: String = ""
) : DatabaseConfiguration {
    companion object {
        private const val DATABASENAME_PROPERTY_NAME: String = "at.michaelfoidl.korm.databaseName"
        private const val DATABASE_VERSION_PROPERTY_NAME: String = "at.michaelfoidl.korm.databaseVersion"
        private const val DATABASE_PATH_PROPERTY_NAME: String = "at.michaelfoidl.korm.databasePath"
        private const val USERNAME_PROPERTY_NAME: String = "at.michaelfoidl.korm.username"
        private const val PASSWORD_PROPERTY_NAME: String = "at.michaelfoidl.korm.password"

        fun fromProperties(properties: Properties): DatabaseConfiguration {
            return DefaultDatabaseConfiguration(
                    databaseName = properties[DATABASENAME_PROPERTY_NAME].toString(),
                    databaseVersion = properties[DATABASE_VERSION_PROPERTY_NAME].toString().toLong(),
                    databasePath = properties[DATABASE_PATH_PROPERTY_NAME].toString(),
                    username = properties[USERNAME_PROPERTY_NAME].toString(),
                    password = properties[PASSWORD_PROPERTY_NAME].toString()
            )
        }

        fun update(configuration: DatabaseConfiguration, databaseVersion: Long): DatabaseConfiguration {
            return DefaultDatabaseConfiguration(
                    databaseName = configuration.databaseName,
                    databaseVersion = databaseVersion,
                    databasePath = configuration.databasePath,
                    username = configuration.username,
                    password = configuration.password
            )
        }
    }
}