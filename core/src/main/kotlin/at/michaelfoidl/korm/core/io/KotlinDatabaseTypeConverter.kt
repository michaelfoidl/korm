/*
 * korm
 *
 * Copyright (c) 2018, Michael Foidl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.michaelfoidl.korm.core.io

object KotlinDatabaseTypeConverter {
    private val JAVA_KOTLIN_TYPEMAP: HashMap<String, String> = hashMapOf(
            Pair("java.lang.String", "kotlin.String"),
            Pair("java.Long", "kotlin.Long"),
            Pair("java.Integer", "kotlin.Int"),
            Pair("java.Boolean", "kotlin.Boolean")
    )

    private val KOTLIN_DB_TYPEMAP: HashMap<String, String> = hashMapOf(
            Pair("kotlin.String", "varchar"),
            Pair("kotlin.Long", "long"),
            Pair("kotlin.Int", "integer"),
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