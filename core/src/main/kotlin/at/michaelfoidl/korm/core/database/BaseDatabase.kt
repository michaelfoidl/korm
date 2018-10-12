package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.ConnectionProvider
import at.michaelfoidl.korm.core.DatabaseSchema
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.core.migrations.InitialMigration
import at.michaelfoidl.korm.core.migrations.MasterTable
import at.michaelfoidl.korm.core.runtime.ClassFetcher
import at.michaelfoidl.korm.interfaces.Database
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.zaxxer.hikari.HikariConfig
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.selectAll
import java.io.File
import kotlin.reflect.KClass

abstract class BaseDatabase(
        vararg entities: KClass<*>
) : Database {
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
    private val migrationFetcher: Cached<ClassFetcher> = Cached {
        ClassFetcher(File(IOOracle.getMigrationFolderPath(this.configuration)))
    }
    private val tableFetcher: Cached<ClassFetcher> = Cached {
        ClassFetcher(File(IOOracle.getTableFolderPath(this.configuration)))
    }
    protected var connectionProvider: ConnectionProvider = ConnectionProvider(this.hikariConfiguration.value)
    protected var schema: DatabaseSchema =
            DatabaseSchema(entities.map {
                this.tableFetcher.value.fetchTable(it, this.configuration.rootPackage)
            })

    protected abstract val configuration: KormConfiguration
    protected abstract fun provideHikariConfig(configuration: KormConfiguration): HikariConfig

    override fun connect(): DatabaseConnection {
        migrate()
        return this.connectionProvider.provideConnection()
    }

    protected fun migrate() {
        ensureThatInitialized()
        val targetVersion: Long = this.configuration.databaseVersion
        var actualVersion: Long = this.currentVersion.value
        while (targetVersion > actualVersion) {
            this.migrationFetcher.value.fetchMigration(this.configuration.databaseName, this.configuration.migrationPackage, actualVersion)
                    .up(this.connectionProvider.provideConnection())
            actualVersion++
        }
        while (targetVersion < actualVersion) {
            this.migrationFetcher.value.fetchMigration(this.configuration.databaseName, this.configuration.migrationPackage, actualVersion)
                    .down(this.connectionProvider.provideConnection())
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