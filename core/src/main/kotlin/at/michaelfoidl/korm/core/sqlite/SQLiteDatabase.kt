package at.michaelfoidl.korm.core.sqlite

import at.michaelfoidl.korm.core.Database
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class SQLiteDatabase(
        version: Long,
        path: String,
        user: String = "",
        password: String = "",
        vararg entities: KClass<*>
) : Database(
        version,
        provideConfiguration(path, user, password),
        *entities) {

    companion object {
        protected fun provideConfiguration(path: String, user: String, password: String): HikariConfig {
            val config = HikariConfig()
            config.jdbcUrl = "jdbc:sqlite:$path"
            config.username = user
            config.password = password

            return config
        }
    }
}