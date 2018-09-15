package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Table

class DatabaseSchema(
        val entities: Collection<Table>
)