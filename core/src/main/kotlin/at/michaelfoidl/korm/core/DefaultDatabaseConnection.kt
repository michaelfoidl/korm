package at.michaelfoidl.korm.core

import com.zaxxer.hikari.HikariDataSource
import at.michaelfoidl.korm.core.exceptions.DoubleInitializationException
import at.michaelfoidl.korm.core.exceptions.ExecutionException
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DefaultDatabaseConnection(
        dataSource: HikariDataSource? = null
) : DatabaseConnection {

    private lateinit var dataSource: HikariDataSource
    private var isInitialized: Boolean = false
    private var isClosed: Boolean = false

    init {
        if (dataSource != null) {
            initialize(dataSource)
        }
    }

    override fun initialize(dataSource: HikariDataSource) {
        if (this.isInitialized) {
            throw DoubleInitializationException("DefaultDatabaseConnection has already been initialized.")
        }
        this.dataSource = dataSource
        ExposedAdapter.connect(this.dataSource)
        this.isInitialized = true
    }

    override fun close() {
        this.isClosed = true
    }

    override fun executeInTransaction(action: () -> Unit) {
        try {
            execute {
                try {
                    transaction(Connection.TRANSACTION_READ_UNCOMMITTED, 1) {
                        action()
                    }
                } catch (e: Exception) {
                    throw ExecutionException("Could not execute action.", e)
                }
            }
        } catch (exception: ExecutionException) {
            throw exception
        }
    }

    override val isValid: Boolean
        get() {
            return this.isInitialized && !this.isClosed
        }

    private fun ensureThatConnectionIsValid() {
        if (!this.isInitialized) {
            throw IllegalStateException("DefaultDatabaseConnection must be initialized before using it.")
        }

        if (this.isClosed) {
            throw IllegalStateException("DefaultDatabaseConnection must not be closed.")
        }
    }

    private fun execute(action: () -> Unit) {
        ensureThatConnectionIsValid()
        action()
        close()
    }
}