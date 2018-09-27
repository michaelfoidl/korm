package at.michaelfoidl.korm.integrationTests.database

import at.michaelfoidl.korm.core.sqlite.SQLiteInMemoryDatabase
import at.michaelfoidl.korm.integrationTests.entities.EntityOne
import at.michaelfoidl.korm.integrationTests.entities.EntityTwo

abstract class TestDatabaseV2: SQLiteInMemoryDatabase(EntityOne::class, EntityTwo::class)