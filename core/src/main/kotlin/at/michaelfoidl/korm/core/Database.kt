package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Table

abstract class Database(
        protected val connectionString: String,
        protected val driver: String,
        protected vararg val entities: Table
) {
    protected var connection: DatabaseConnection? = null

    fun connect(user: String = "", password: String = ""): DatabaseConnection {
        if (connection == null) {
            doConnect(this.connectionString, this.driver, user, password)
            this.connection = DatabaseConnection()
        }

        return this.connection!!
    }

    protected open fun doConnect(connectionString: String, driver: String, user: String = "", password: String = "") {
        ExposedAdapter.connect(connectionString, driver, user, password)
    }

    protected fun create() {
        connect().execute {
            ExposedAdapter.create(*this.entities)
        }
    }

    protected fun destroy() {
        connect().execute {
            ExposedAdapter.drop(*entities)
        }
    }
}