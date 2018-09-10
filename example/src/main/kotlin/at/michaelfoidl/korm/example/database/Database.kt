package at.michaelfoidl.korm.example.database

import at.michaelfoidl.korm.core.SQLiteDatabase
import test.EntityOneTable
import test.EntityTwoTable

class Database: SQLiteDatabase(EntityOneTable, EntityTwoTable) {
}