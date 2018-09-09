package at.michaelfoidl.korm.example

import at.michaelfoidl.korm.annotations.ColumnName
import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.annotations.ForeignKey
import at.michaelfoidl.korm.annotations.PrimaryKey

@Entity
class EntityOne private constructor() {
    constructor(id: Long, simpleProperty: String, complexProperty: EntityTwo) : this() {
        this.id = id
        this.simpleProperty = simpleProperty
        this.complexProperty = complexProperty
    }

    @PrimaryKey
    var id: Long = -1

    @ColumnName("simple")
    lateinit var simpleProperty: String

    @ForeignKey(EntityTwo::class, "id")
    lateinit var complexProperty: EntityTwo
}