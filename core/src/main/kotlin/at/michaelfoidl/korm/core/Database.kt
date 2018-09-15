package at.michaelfoidl.korm.core

import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.core.migrations.MasterTable
import com.zaxxer.hikari.HikariConfig
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import kotlin.reflect.KClass

abstract class Database(
        protected val version: Long,
        protected val configuration: HikariConfig,
        vararg entities: KClass<*>
) {
    protected var doesDatabaseExist: Boolean = false
    protected var connectionProvider: ConnectionProvider = ConnectionProvider(configuration)
    protected var schema: DatabaseSchema =
            DatabaseSchema(entities.map {
                ClassFetcher.fetchTable(it)
            })
    protected val actualVersion = Cached {
        var version: Long = -1
        this.connectionProvider.provideConnection().executeInTransaction {
            version = MasterTable
                    .slice(MasterTable.version.max())
                    .selectAll()
                    .first()[MasterTable.version]
        }
        version
    }

    fun authenticate(user: String = "", password: String = "") {
        this.configuration.username = user
        this.configuration.password = password
        this.connectionProvider.configure(this.configuration)
    }

    fun connect(): DatabaseConnection {
        migrate()
        return this.connectionProvider.provideConnection()
    }

    protected fun migrate() {
        var actualVersion = this.actualVersion.value
        while (this.version > actualVersion) {
            ClassFetcher.fetchMigration(actualVersion).up(this.connectionProvider.provideConnection(), actualVersion + 1)
            actualVersion++
        }
        while (this.version < actualVersion) {
            ClassFetcher.fetchMigration(actualVersion).down(this.connectionProvider.provideConnection(), actualVersion + 1)
            actualVersion--
        }
        this.actualVersion.invalidate()
    }
}