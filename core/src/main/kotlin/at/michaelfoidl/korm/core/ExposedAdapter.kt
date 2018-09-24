package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import javax.sql.DataSource

object ExposedAdapter {
    fun connect(dataSource: DataSource) {
        Database.connect(dataSource)
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