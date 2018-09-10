package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnection {
    internal fun execute(action: () -> Unit) {
        try {
            transaction {
                action()
            }
        } catch (e: Exception) {
            throw ExecutionException("Could not execute action.", e)
        }
    }
}