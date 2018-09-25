package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.ClassFetcher
import at.michaelfoidl.korm.core.ConnectionProvider
import at.michaelfoidl.korm.core.DatabaseConnection
import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.configuration.KormConfiguration
import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.core.migrations.InitialMigration
import at.michaelfoidl.korm.core.migrations.MasterTable
import com.zaxxer.hikari.HikariConfig
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import kotlin.reflect.KClass

abstract class Database(
        vararg entities: KClass<*>
) {
    protected var doesDatabaseExist: Boolean = false
    protected val hikariConfiguration: Cached<HikariConfig> = Cached {
        provideHikariConfig(this.configuration)
    }
    protected val currentVersion: Cached<Long> = Cached {
        var version: Long = -1
        this.connectionProvider.provideConnection().executeInTransaction {
            val maxVersion = MasterTable.version.max().alias("maxVersion")
            version = MasterTable
                    .slice(maxVersion)
                    .selectAll()
                    .first()[maxVersion]!!
        }
        version
    }
    protected var connectionProvider: ConnectionProvider = ConnectionProvider(this.hikariConfiguration.value)
    protected var schema: DatabaseSchema =
            DatabaseSchema(entities.map {
                ClassFetcher.fetchTable(it)
            })

    protected abstract val configuration: KormConfiguration
    protected abstract fun provideHikariConfig(configuration: KormConfiguration): HikariConfig

    fun connect(): DatabaseConnection {
        migrate()
        return this.connectionProvider.provideConnection()
    }

    protected fun migrate() {
        ensureThatInitialized()
        val targetVersion: Long = this.configuration.databaseVersion
        var actualVersion: Long = this.currentVersion.value
        while (targetVersion > actualVersion) {
            ClassFetcher.fetchMigration(actualVersion).up(this.connectionProvider.provideConnection())
            actualVersion++
        }
        while (targetVersion < actualVersion) {
            ClassFetcher.fetchMigration(actualVersion).down(this.connectionProvider.provideConnection())
            actualVersion--
        }
        this.currentVersion.invalidate()
    }

    protected fun ensureThatInitialized() {
        var isInitialized = false
        this.connectionProvider.provideConnection().executeInTransaction {
            isInitialized = MasterTable.exists()
        }
        if (!isInitialized) {
            InitialMigration().up(this.connectionProvider.provideConnection())
        }
    }
}