package at.michaelfoidl.korm.core

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import javax.sql.DataSource

internal object ExposedAdapter {
    fun connect(connectionString: String, driver: String, user: String = "", password: String = "") {
        Database.connect(connectionString, driver, user, password)
    }

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