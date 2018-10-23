package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.database.BaseDatabase
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteDatabase(
        databaseName: String,
        vararg entities: KClass<*>
) : BaseDatabase(databaseName, *entities) {

    override fun provideHikariConfig(configuration: DatabaseConfiguration): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:${configuration.databasePath}"
        config.username = configuration.username
        config.password = configuration.password

        return config
    }
}