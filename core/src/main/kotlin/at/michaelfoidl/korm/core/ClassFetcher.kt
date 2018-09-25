package at.michaelfoidl.korm.core

import at.michaelfoidl.korm.core.migrations.Migration
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KClass

object ClassFetcher {
    fun fetchTable(entityClass: KClass<*>): Table {
        val tableName = entityClass.simpleName + "Table"
        return (Class.forName("test.$tableName").kotlin as KClass<Table>).objectInstance!!
    }

    fun fetchMigration(version: Long): Migration {
        val migrationName = "Migration_${version}_${version + 1}"
        return Class.forName(migrationName).newInstance() as Migration
    }
}