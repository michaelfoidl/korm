package at.michaelfoidl.korm.integrationTests.entities

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.annotations.PrimaryKey

@Entity
class EntityTwo private constructor() {
    constructor(id: Long, simpleProperty: String) : this() {
        this.id = id
        this.simpleProperty = simpleProperty
    }

    @PrimaryKey
    var id: Long = -1
    lateinit var simpleProperty: String
}