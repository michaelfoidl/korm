package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.configuration.ConfigurationProvider
import at.michaelfoidl.korm.core.connection.ConnectionProvider
import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.core.runtime.ClassFetcher
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.zaxxer.hikari.HikariConfig
import kotlin.reflect.KClass

abstract class BaseDatabase internal constructor(
        kormConfiguration: KormConfiguration? = null,
        databaseConfiguration: DatabaseConfiguration? = null,
        hikariConfiguration: HikariConfig? = null,
        classFetcher: ClassFetcher? = null,
        connectionProvider: ConnectionProvider? = null,
        databaseState: DatabaseState? = null,
        vararg entities: KClass<*>
) : Database {

    constructor(
            vararg entities: KClass<*>
    ) : this(
            null,
            null,
            null,
            null,
            null,
            null,
            *entities
    )

    private val _kormConfiguration: KormConfiguration? = kormConfiguration
    private val _databaseConfiguration: DatabaseConfiguration? = databaseConfiguration
    private val _hikariConfiguration: HikariConfig? = hikariConfiguration
    private val _classFetcher: ClassFetcher? = classFetcher
    private val _connectionProvider: ConnectionProvider? = connectionProvider
    private val _databaseState: DatabaseState? = databaseState

//    protected var schema: DatabaseSchema = DatabaseSchema.fromEntityCollection(entities.toList())

    protected val kormConfiguration: KormConfiguration
        get() = this._kormConfiguration ?: ConfigurationProvider.provideKormConfiguration()

    protected val databaseConfiguration: DatabaseConfiguration
        get() = this._databaseConfiguration ?: ConfigurationProvider.provideDatabaseConfiguration(this::class)

    private val hikariConfiguration: HikariConfig
        get() = this._hikariConfiguration ?: provideHikariConfiguration(this.databaseConfiguration)

    private val classFetcher: ClassFetcher
        get() = this._classFetcher ?: ClassFetcher(this.kormConfiguration)

    private val connectionProvider: ConnectionProvider
        get() = this._connectionProvider ?: ConnectionProvider(this.hikariConfiguration)

    private val databaseState: DatabaseState
        get() = this._databaseState ?: DatabaseState(connectionProvider)

    private val currentVersion: Cached<Long> = Cached {
        this.databaseState.getCurrentVersion()
    }

    protected abstract fun provideHikariConfiguration(configuration: DatabaseConfiguration): HikariConfig

    override fun connect(): DatabaseConnection {
        migrate()
        return this.connectionProvider.provideConnection()
    }

    protected fun migrate() {
        ensureThatDatabaseIsInitialized()
        val targetVersion: Long = this.databaseConfiguration.databaseVersion
        var actualVersion: Long = this.currentVersion.value
        while (targetVersion > actualVersion) {
            val updatedConfiguration = this.databaseConfiguration.update(actualVersion)
            this.classFetcher.fetchMigration(updatedConfiguration)
                    .up(this.connectionProvider.provideConnection())
            actualVersion++
        }
        while (targetVersion < actualVersion) {
            val updatedConfiguration = this.databaseConfiguration.update(actualVersion - 1)
            this.classFetcher.fetchMigration(updatedConfiguration)
                    .down(this.connectionProvider.provideConnection())
            actualVersion--
        }
        this.currentVersion.invalidate()
    }

    protected fun initializeDatabase() {
        this.databaseState.initializeDatabase()
    }

    private fun ensureThatDatabaseIsInitialized() {
        if (!this.databaseState.isDatabaseInitialized()) {
            this.databaseState.initializeDatabase()
        }
    }
}