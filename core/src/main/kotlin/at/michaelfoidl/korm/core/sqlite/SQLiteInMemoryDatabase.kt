package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.database.BaseDatabase
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteInMemoryDatabase(
        databaseName: String,
        vararg entities: KClass<*>
) : BaseDatabase(databaseName, *entities) {

    override fun provideHikariConfig(configuration: DatabaseConfiguration): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:file:test?mode=memory&cache=shared"

        return config
    }
}