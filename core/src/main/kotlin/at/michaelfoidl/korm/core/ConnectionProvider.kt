package at.michaelfoidl.korm.core

import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class ConnectionProvider(
        private var configuration: HikariConfig
) {
    private var connection = Cached {
        DefaultDatabaseConnection(HikariDataSource(this.configuration))
    }

    fun provideConnection(): DatabaseConnection {
        if (!this.connection.value.isValid) {
            this.connection.invalidate()
        }
        return this.connection.value
    }

    fun configure(configuration: HikariConfig): ConnectionProvider {
        this.configuration = configuration
        this.connection.invalidate()
        return this
    }
}