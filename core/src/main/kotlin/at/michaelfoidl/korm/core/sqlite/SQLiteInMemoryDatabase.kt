package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.Database
import com.zaxxer.hikari.HikariConfig
import org.jetbrains.exposed.sql.Table

abstract class SQLiteInMemoryDatabase(
        vararg entities: Table
) : Database(
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