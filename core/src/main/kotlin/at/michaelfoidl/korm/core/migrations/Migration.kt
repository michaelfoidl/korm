package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
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
}