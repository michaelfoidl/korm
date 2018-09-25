package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.database.Database
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteInMemoryDatabase(
        vararg entities: KClass<*>
) : Database(*entities) {

    override fun provideHikariConfig(configuration: KormConfiguration): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:file:test?mode=memory&cache=shared"

        return config
    }
}