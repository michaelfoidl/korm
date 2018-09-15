package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
import org.jetbrains.exposed.sql.update

abstract class Migration {

    abstract fun up(connection: DatabaseConnection, targetVersion: Long)

    abstract fun down(connection: DatabaseConnection, targetVersion: Long)

    protected fun updateVersion(targetVersion: Long) {
        MasterTable.update({ MasterTable.version neq targetVersion }) {
            it[version] = targetVersion
        }
    }
}