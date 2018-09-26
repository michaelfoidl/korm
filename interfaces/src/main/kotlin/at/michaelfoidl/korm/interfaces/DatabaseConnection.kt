package at.michaelfoidl.korm.interfaces

import com.zaxxer.hikari.HikariDataSource

interface DatabaseConnection {

    fun initialize(dataSource: HikariDataSource)

    fun close()

    fun executeInTransaction(action: () -> Unit)

    val isValid: Boolean
}