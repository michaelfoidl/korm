package at.michaelfoidl.korm.core.schema

import kotlin.reflect.KClass

class DatabaseSchema private constructor(
        val tables: Collection<Table>
) {
    companion object {
        fun fromConfigurationFile(): DatabaseSchema {
            TODO("not implemented")
        }

        fun fromEntityCollection(entities: Collection<KClass<*>>): DatabaseSchema {
            val tableBuilders: MutableCollection<TableBuilder> = entities.map { TableBuilder(it) }.toMutableList()
            val tables: MutableCollection<Table> = ArrayList()
            val columns: MutableCollection<Column> = ArrayList()

            while (!tableBuilders.isEmpty()) {
                val builder = tableBuilders.first()
                tableBuilders.remove(builder)
                if (builder.canBeResolved(tables)) {
                    val newTable = builder.toTable(tables, columns)
                    tables.add(newTable)
                    columns.addAll(newTable.columns)
                } else {
                    tableBuilders.add(builder)
                }
            }

            return DatabaseSchema(tables)
        }
    }
}