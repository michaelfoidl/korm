package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.DatabaseSchema
import org.jetbrains.exposed.sql.*

object DatabaseSchemaAnalyzer {
    fun getMissingTables(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Table> {
        return targetSchema.entities.filter { targetTable ->
            !currentSchema.entities.any { currentTable ->
                currentTable.tableName == targetTable.tableName
            }
        }
    }

    fun getMissingColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Column<*>> {
        return targetSchema.entities
                .filter { targetTable ->
                    currentSchema.entities.any { currentTable ->
                        currentTable.tableName == targetTable.tableName
                    }
                }
                .flatMap { targetTable ->
                    targetTable.columns.filter { targetColumn ->
                        !currentSchema.entities.find { currentTable ->
                            currentTable.tableName == targetTable.tableName
                        }!!.columns.any { currentColumn ->
                            currentColumn.name == targetColumn.name
                        }
                    }
                }
    }

    fun getDroppedTables(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Table> {
        return currentSchema.entities.filter { currentTable ->
            !targetSchema.entities.any { targetTable ->
                targetTable.tableName == currentTable.tableName
            }
        }
    }

    fun getDroppedColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Column<*>> {
        return currentSchema.entities
                .filter { currentTable ->
                    targetSchema.entities.any { targetTable ->
                        targetTable.tableName == currentTable.tableName
                    }
                }
                .flatMap { currentTable ->
                    currentTable.columns.filter { currentColumn ->
                        !targetSchema.entities.find { targetTable ->
                            targetTable.tableName == currentTable.tableName
                        }!!.columns.any { targetColumn ->
                            targetColumn.name == currentColumn.name
                        }
                    }
                }
    }

    fun getChangedColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Pair<Column<*>, Column<*>>> {
        return currentSchema.entities
                .filter { currentTable ->
                    targetSchema.entities.any { targetTable ->
                        targetTable.tableName == currentTable.tableName
                    }
                }
                .flatMap { currentTable ->
                    currentTable.columns
                            .map { currentColumn ->
                                Pair(currentColumn,
                                        targetSchema.entities.find { targetTable ->
                                            targetTable.tableName == currentTable.tableName
                                        }!!.columns.find { targetColumn ->
                                            !areColumnTypesEqual(currentColumn.columnType, targetColumn.columnType) && currentColumn.name == targetColumn.name
                                        }
                                )
                            }
                            .filter { it.second != null }
                            .map { it as Pair<Column<*>, Column<*>> }
                }
    }

    private fun <T : IColumnType, U : IColumnType> areColumnTypesEqual(a: T, b: U): Boolean {
        return when {
            a is CharacterColumnType && b is CharacterColumnType -> {
                true
            }
            a is IntegerColumnType && b is IntegerColumnType -> {
                true
            }
            a is LongColumnType && b is LongColumnType -> {
                true
            }
            a is FloatColumnType && b is FloatColumnType -> {
                true
            }
            a is DecimalColumnType && b is DecimalColumnType -> {
                a.precision == b.precision && a.scale == b.scale
            }
            a is EnumerationColumnType<*> && b is EnumerationColumnType<*> -> {
                a.klass.canonicalName == b.klass.canonicalName
            }
            a is DateColumnType && b is DateColumnType -> {
                a.time == b.time
            }
            a is VarCharColumnType && b is VarCharColumnType -> {
                a.colLength == b.colLength && a.collate == b.collate
            }
            a is TextColumnType && b is TextColumnType -> {
                a.collate == b.collate
            }
            a is BinaryColumnType && b is BinaryColumnType -> {
                a.length == b.length
            }
            a is BlobColumnType && b is BlobColumnType -> {
                true
            }
            a is BooleanColumnType && b is BooleanColumnType -> {
                true
            }
            a is UUIDColumnType && b is UUIDColumnType -> {
                true
            }
            else -> {
                false
            }
        }
    }
}