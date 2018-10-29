package at.michaelfoidl.korm.core.migrations

import at.michaelfoidl.korm.core.schema.Column
import at.michaelfoidl.korm.core.schema.DatabaseSchema
import at.michaelfoidl.korm.core.schema.Table

object DatabaseSchemaAnalyzer {
    fun getMissingTables(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Table> {
        return targetSchema.tables.filter { targetTable ->
            !currentSchema.tables.any { currentTable ->
                currentTable.name == targetTable.name
            }
        }
    }

    fun getMissingColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Column> {
        return targetSchema.tables
                .filter { targetTable ->
                    currentSchema.tables.any { currentTable ->
                        currentTable.name == targetTable.name
                    }
                }
                .flatMap { targetTable ->
                    targetTable.columns.filter { targetColumn ->
                        !currentSchema.tables.find { currentTable ->
                            currentTable.name == targetTable.name
                        }!!.columns.any { currentColumn ->
                            currentColumn.name == targetColumn.name
                        }
                    }
                }
    }

    fun getDroppedTables(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Table> {
        return currentSchema.tables.filter { currentTable ->
            !targetSchema.tables.any { targetTable ->
                targetTable.name == currentTable.name
            }
        }
    }

    fun getDroppedColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Column> {
        return currentSchema.tables
                .filter { currentTable ->
                    targetSchema.tables.any { targetTable ->
                        targetTable.name == currentTable.name
                    }
                }
                .flatMap { currentTable ->
                    currentTable.columns.filter { currentColumn ->
                        !targetSchema.tables.find { targetTable ->
                            targetTable.name == currentTable.name
                        }!!.columns.any { targetColumn ->
                            targetColumn.name == currentColumn.name
                        }
                    }
                }
    }

    fun getChangedColumns(currentSchema: DatabaseSchema, targetSchema: DatabaseSchema): Collection<Pair<Column, Column>> {
        return currentSchema.tables
                .filter { currentTable ->
                    targetSchema.tables.any { targetTable ->
                        targetTable.name == currentTable.name
                    }
                }
                .flatMap { currentTable ->
                    currentTable.columns
                            .map { currentColumn ->
                                Pair(currentColumn,
                                        targetSchema.tables.find { targetTable ->
                                            targetTable.name == currentTable.name
                                        }!!.columns.find { targetColumn ->
                                            currentColumn.name == targetColumn.name && !Column.compare(currentColumn, targetColumn).isUnchanged
                                        }
                                )
                            }
                            .filter { it.second != null }
                            .map { it as Pair<Column, Column> }
                }
    }
}