package at.michaelfoidl.korm.core.migrations

import org.jetbrains.exposed.sql.Table

object MasterTable: Table() {
    val id = long("id").primaryKey()
    val version = long("version")
}