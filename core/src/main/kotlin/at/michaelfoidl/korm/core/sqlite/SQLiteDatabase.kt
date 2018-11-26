package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.database.BaseDatabase
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteDatabase(
        vararg entities: KClass<*>
) : BaseDatabase(*entities) {

    override fun provideHikariConfiguration(configuration: DatabaseConfiguration): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:${configuration.databasePath}"
        config.username = configuration.username
        config.password = configuration.password

        return config
    }
}