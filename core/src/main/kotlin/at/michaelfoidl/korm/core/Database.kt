package at.michaelfoidl.korm.core

import com.zaxxer.hikari.HikariConfig
import org.jetbrains.exposed.sql.Table

abstract class Database(
        protected val configuration: HikariConfig,
        protected vararg val entities: Table
) {
    protected var doesDatabaseExist: Boolean = false
    protected var connectionProvider: ConnectionProvider = ConnectionProvider(configuration)


    fun authenticate(user: String = "", password: String = "") {
        this.configuration.username = user
        this.configuration.password = password
        this.connectionProvider.configure(this.configuration)
    }

    fun connect(): DatabaseConnection {
        this.connectionProvider.provideConnection().executeInTransaction {
            ExposedAdapter.createMissing(*this.entities)
        }

        return this.connectionProvider.provideConnection()
    }

    protected fun create() {
        connect().executeInTransaction {
            ExposedAdapter.create(*this.entities)
        }
    }

    protected fun destroy() {
        connect().executeInTransaction {
            ExposedAdapter.drop(*entities)
        }
    }
}