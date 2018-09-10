package at.michaelfoidl.korm.integrationTests.database

import at.michaelfoidl.korm.core.SQLiteDatabase
import test.EntityOneTable
import test.EntityTwoTable

class Database: SQLiteDatabase(EntityOneTable, EntityTwoTable) {
}