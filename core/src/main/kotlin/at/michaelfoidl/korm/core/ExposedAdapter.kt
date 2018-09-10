package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table

internal object ExposedAdapter {
    fun connect(connectionString: String, driver: String, user: String = "", password: String = "") {
        Database.connect(connectionString, driver, user, password)
    }

    fun create(vararg entities: Table) {
        SchemaUtils.create(*entities)
    }

    fun drop(vararg entities: Table) {
        SchemaUtils.drop(*entities)
    }
}