package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

abstract class SQLiteDatabase(
        protected vararg val entities: Table
) : Database("jdbc:sqlite:file:test?mode=memory&cache=shared") {

    override fun create() {
        connect()

        transaction(Connection.TRANSACTION_READ_UNCOMMITTED, 1) {
            SchemaUtils.create(*entities)
        }
    }

    override fun connect(): org.jetbrains.exposed.sql.Database {
        return org.jetbrains.exposed.sql.Database.connect(this.connectionString, "org.sqlite.JDBC")
    }
}