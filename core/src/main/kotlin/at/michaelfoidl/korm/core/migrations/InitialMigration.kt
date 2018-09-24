package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseConnection
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert

class InitialMigration : Migration(1) {
    override fun up(connection: DatabaseConnection) {
        connection.executeInTransaction {
            createTable(MasterTable)
            MasterTable.insert {
                it[version] = targetVersion
            }
        }
    }

    override fun down(connection: DatabaseConnection) {
        connection.executeInTransaction {
            SchemaUtils.drop(MasterTable)
        }
    }
}