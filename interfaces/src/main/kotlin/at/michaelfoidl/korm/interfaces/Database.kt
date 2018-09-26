package at.michaelfoidl.korm.interfaces

interface Database {
    fun connect(): DatabaseConnection
}