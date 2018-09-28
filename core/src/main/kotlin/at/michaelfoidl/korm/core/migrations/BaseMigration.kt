package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.interfaces.DatabaseConnection
import at.michaelfoidl.korm.interfaces.Migration
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.update

abstract class BaseMigration(
        protected val targetVersion: Long
) : Migration {

    abstract override fun up(connection: DatabaseConnection)

    abstract override fun down(connection: DatabaseConnection)

    protected fun updateVersion() {
        MasterTable.update({ MasterTable.version neq targetVersion }) {
            it[version] = targetVersion
        }
    }

    protected fun createTable(table: Table) {
        table.createStatement().forEach {
            TransactionManager.current().exec(it)
        }
    }
}