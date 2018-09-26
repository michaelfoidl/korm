package at.michaelfoidl.korm.interfaces

import kotlin.reflect.KClass

interface KormConfiguration {
    val databaseType: DatabaseType
    val databaseVersion: Long
    val databaseInterface: KClass<out Database>
    val databasePath: String?
    val username: String
    val password: String
    val migrationPackage: String
    val databasePackage: String
    val rootPackage: String
    val rootDirectory: String
}