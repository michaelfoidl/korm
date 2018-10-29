package at.michaelfoidl.korm.annotations

@Target(AnnotationTarget.FIELD)
annotation class PrimaryKey(val autoIncrement: Boolean = true)