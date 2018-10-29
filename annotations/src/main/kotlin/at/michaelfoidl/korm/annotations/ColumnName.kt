package at.michaelfoidl.korm.annotations

@Target(AnnotationTarget.FIELD)
annotation class ColumnName(val columnName: String)