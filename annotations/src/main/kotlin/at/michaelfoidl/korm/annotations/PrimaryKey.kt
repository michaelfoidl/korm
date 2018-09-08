package at.michaelfoidl.korm.annotations

@Target(AnnotationTarget.PROPERTY)
annotation class PrimaryKey(val autoIncrement: Boolean = true)