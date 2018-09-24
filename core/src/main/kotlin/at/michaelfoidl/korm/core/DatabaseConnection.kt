package at.michaelfoidl.korm.core

import com.zaxxer.hikari.HikariDataSource
import at.michaelfoidl.korm.core.exceptions.DoubleInitializationException
import at.michaelfoidl.korm.core.exceptions.ExecutionException
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DatabaseConnection(
        dataSource: HikariDataSource? = null
) {

    private lateinit var dataSource: HikariDataSource
    private var isInitialized: Boolean = false
    private var isClosed: Boolean = false

    init {
        if (dataSource != null) {
            initialize(dataSource)
        }
    }

    fun initialize(dataSource: HikariDataSource) {
        if (this.isInitialized) {
            throw DoubleInitializationException("DatabaseConnection has already been initialized.")
        }
        this.dataSource = dataSource
        ExposedAdapter.connect(this.dataSource)
        this.isInitialized = true
    }

    fun close() {
        this.isClosed = true
    }

    fun executeInTransaction(action: () -> Unit) {
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

    val isValid: Boolean
        get() {
            return this.isInitialized && !this.isClosed
        }

    private fun ensureThatConnectionIsValid() {
        if (!this.isInitialized) {
            throw IllegalStateException("DatabaseConnection must be initialized before using it.")
        }

        if (this.isClosed) {
            throw IllegalStateException("DatabaseConnection must not be closed.")
        }
    }

    private fun execute(action: () -> Unit) {
        ensureThatConnectionIsValid()
        action()
        close()
    }
}