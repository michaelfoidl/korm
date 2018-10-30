package at.michaelfoidl.korm.core.testUtils.database

import at.michaelfoidl.korm.annotations.Database
import at.michaelfoidl.korm.core.sqlite.SQLiteInMemoryDatabase

@Database
abstract class DatabaseInterface: SQLiteInMemoryDatabase()