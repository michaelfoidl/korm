package at.michaelfoidl.korm.core.exposed

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import javax.sql.DataSource


object ExposedAdapter {

    fun connect(dataSource: DataSource) {
        Database.connect(dataSource)
    }

    fun transaction(transactionLevel: Int, attempts: Int, action: () -> Unit) {
        org.jetbrains.exposed.sql.transactions.transaction(transactionLevel, attempts) {
            action()
        }
    }

    fun create(vararg entities: Table) {
        SchemaUtils.create(*entities)
    }

    fun createMissing(vararg entities: Table) {
        SchemaUtils.createMissingTablesAndColumns(*entities)
    }

    fun drop(vararg entities: Table) {
        SchemaUtils.drop(*entities)
    }
}