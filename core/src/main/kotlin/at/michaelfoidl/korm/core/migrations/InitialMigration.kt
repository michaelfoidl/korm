package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert

class InitialMigration : Migration(TARGET_VERSION) {
    override fun up(connection: DatabaseConnection) {
        connection.executeInTransaction {
            createTable(MasterTable)
            MasterTable.insert {
                it[version] = TARGET_VERSION
            }
        }
    }

    override fun down(connection: DatabaseConnection) {
        connection.executeInTransaction {
            SchemaUtils.drop(MasterTable)
        }
    }

    companion object {
        const val TARGET_VERSION: Long = 1
    }
}