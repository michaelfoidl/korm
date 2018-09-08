package at.michaelfoidl.korm.annotations

@Target(AnnotationTarget.CLASS)
annotation class Entity(val tableName: String = "")