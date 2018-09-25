package at.michaelfoidl.korm.integrationTests.database

import at.michaelfoidl.korm.core.sqlite.SQLiteInMemoryDatabase
import at.michaelfoidl.korm.integrationTests.entities.EntityOne

abstract class TestDatabaseV1: SQLiteInMemoryDatabase(EntityOne::class)