package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.Database
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteInMemoryDatabase(
        version: Long,
        vararg entities: KClass<*>
) : Database(
        version,
        provideConfiguration(),
        *entities
) {

    companion object {
        protected fun provideConfiguration(): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = "jdbc:sqlite:file:test?mode=memory&cache=shared"

            return config
        }
    }
}