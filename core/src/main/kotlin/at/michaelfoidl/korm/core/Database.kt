package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Database

abstract class Database(
        protected val connectionString: String
) {
    protected val db: Database by lazy {
        connect()
    }

    protected abstract fun connect(): Database

    abstract fun create()
}