package at.michaelfoidl.korm.core

import at.michaelfoidl.korm.core.migrations.Migration
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KClass

object ClassFetcher {
    fun fetchTable(entityClass: KClass<*>): Table {
        val tableName = entityClass.simpleName + "Table"
        return Class.forName(tableName).newInstance() as Table
    }

    fun fetchMigration(version: Long): Migration {
        val migrationName = "Migration_$version"
        return Class.forName(migrationName).newInstance() as Migration
    }
}