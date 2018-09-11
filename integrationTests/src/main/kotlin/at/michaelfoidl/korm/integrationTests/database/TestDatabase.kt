package at.michaelfoidl.korm.integrationTests.database

import at.michaelfoidl.korm.core.sqlite.SQLiteInMemoryDatabase
import test.EntityOneTable
import test.EntityTwoTable

class TestDatabase: SQLiteInMemoryDatabase(
        EntityOneTable,
        EntityTwoTable)