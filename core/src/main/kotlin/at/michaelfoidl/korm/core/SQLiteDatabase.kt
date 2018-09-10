package at.michaelfoidl.korm.core

import org.jetbrains.exposed.sql.Table

abstract class SQLiteDatabase(
        vararg entities: Table
) : Database(
        "jdbc:sqlite:file:test?mode=memory&cache=shared",
        "org.sqlite.JDBC",
        *entities)