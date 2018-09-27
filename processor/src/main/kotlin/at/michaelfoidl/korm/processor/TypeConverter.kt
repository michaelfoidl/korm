package at.michaelfoidl.korm.processor

object TypeConverter {
    private val JAVA_KOTLIN_TYPEMAP: HashMap<String, String> = hashMapOf(
            Pair("java.lang.String", "kotlin.String"),
            Pair("java.Long", "kotlin.Long"),
            Pair("java.Integer", "kotlin.Integer"),
            Pair("java.Boolean", "kotlin.Boolean")
    )

    private val KOTLIN_DB_TYPEMAP: HashMap<String, String> = hashMapOf(
            Pair("kotlin.String", "varchar"),
            Pair("kotlin.Long", "long"),
            Pair("kotlin.Integer", "integer"),
            Pair("kotlin.Boolean", "bool")
    )

    fun convertToKotlinType(type: String): String {
        return if (!isKotlinType(type)) {
            JAVA_KOTLIN_TYPEMAP[type] ?: throw IllegalArgumentException("Unknown type: $type.")
        } else {
            type
        }
    }

    fun convertToDatabaseType(type: String): String {
        var t = type
        if (!isKotlinType(t)) {
            t = convertToKotlinType(t)
        }
        return KOTLIN_DB_TYPEMAP[t] ?: throw IllegalArgumentException("Unknown type: $t.")
    }

    private fun isKotlinType(type: String): Boolean {
        return type.startsWith("kotlin")
    }

//    object Test: Table() {
//        val id: Column<Long> = boo("id")
//    }
}