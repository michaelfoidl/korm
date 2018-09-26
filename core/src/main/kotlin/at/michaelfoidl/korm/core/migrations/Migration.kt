package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.interfaces.DatabaseConnection
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.update

abstract class Migration(
        protected val targetVersion: Long
) {

    abstract fun up(connection: DatabaseConnection)

    abstract fun down(connection: DatabaseConnection)

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