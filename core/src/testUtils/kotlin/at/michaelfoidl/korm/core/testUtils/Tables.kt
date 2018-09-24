package at.michaelfoidl.korm.core.testUtils

import org.jetbrains.exposed.sql.Table

object Table : Table("table") {
    val id = long("id").primaryKey()
    val data = varchar("data", 255)
}

object MissingTable : Table("missingTable") {
    val id = long("id").primaryKey()
    val data = varchar("data", 255)
}

object DroppedTable : Table("droppedTable") {
    val id = long("id").primaryKey()
    val data = varchar("data", 255)
}

object TableWithMissingColumn: Table("tableWithMissingColumn") {
    val id = long("id").primaryKey()
}

object TableWithDroppedColumn: Table("tableWithDroppedColumn") {
    val id = long("id").primaryKey()
}

object TableWithChangedColumn: Table("tableWithChangedColumn") {
    val id = long("id").primaryKey()
    val data = varchar("data", 256)
}
