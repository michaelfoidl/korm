package at.michaelfoidl.korm.core.schema

import at.michaelfoidl.korm.core.configuration.ConfigurationProvider
import at.michaelfoidl.korm.core.io.IOOracle
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import java.io.File
import kotlin.reflect.KClass

class DatabaseSchema private constructor(
        val tables: Collection<Table>
) {
    fun toJSON(): String {
        return """
            |{
            |  "tables": [
            |    ${this.tables.joinToString(",\n") { it.toRawJSON(4) }.substring(4)}
            |  ]
            |}
            """.trimMargin()
    }

    companion object {

        fun fromConfigurationFile(): DatabaseSchema {
            return fromConfigurationFile(IOOracle)
        }

        internal fun fromConfigurationFile(ioOracle: IOOracle = IOOracle): DatabaseSchema {
            val configurationFile = ioOracle.getDatabaseSchemaBuilder(ConfigurationProvider.provideKormConfiguration()).sourcePath(false)
            return build(
                    (Parser().parse(configurationFile) as JsonObject).array<JsonObject>("tables")!!
                            .map { TableJsonBuilder(it) }.toMutableList())
        }

        fun fromEntityCollection(entities: Collection<KClass<*>>): DatabaseSchema {
            return fromEntityCollection(entities, TableEntityBuilder.Companion)
        }

        internal fun fromEntityCollection(entities: Collection<KClass<*>>, tableBuilder: TableEntityBuilder.Companion = TableEntityBuilder.Companion): DatabaseSchema {
            return build(entities.map { tableBuilder.create(it) }.toMutableList())
        }

        internal fun build(tableBuilders: MutableCollection<TableBuilder>): DatabaseSchema {
            val tables: MutableCollection<Table> = ArrayList()
            val columns: MutableCollection<Column> = ArrayList()

            while (!tableBuilders.isEmpty()) {
                val builder = tableBuilders.first()
                tableBuilders.remove(builder)
                if (builder.canBeResolved(tables)) {
                    val newTable = builder.toTable(tables, columns)
                    tables.add(newTable)
                    columns.addAll(newTable.allColumns)
                } else {
                    tableBuilders.add(builder)
                }
            }

            return DatabaseSchema(tables)
        }

        internal fun fromTableCollection(tables: Collection<Table>): DatabaseSchema {
            return DatabaseSchema(tables)
        }
    }
}