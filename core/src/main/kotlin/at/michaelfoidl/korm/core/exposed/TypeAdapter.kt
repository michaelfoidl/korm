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

package at.michaelfoidl.korm.core.exposed

import at.michaelfoidl.korm.core.schema.DatabaseType
import org.jetbrains.exposed.sql.*
import java.util.Date
import kotlin.reflect.KClass

internal object TypeAdapter {
    private val exposedTypeMap: HashMap<KClass<*>, DatabaseType> = hashMapOf(
            Pair(CharacterColumnType::class, DatabaseType.Character),
            Pair(IntegerColumnType::class, DatabaseType.Integer),
            Pair(LongColumnType::class, DatabaseType.Long),
            Pair(FloatColumnType::class, DatabaseType.Float),
            Pair(DoubleColumnType::class, DatabaseType.Double),
            Pair(EnumerationColumnType::class, DatabaseType.Enumeration),
            Pair(DateColumnType::class, DatabaseType.Date),
            Pair(VarCharColumnType::class, DatabaseType.Varchar),
            Pair(TextColumnType::class, DatabaseType.Text),
            Pair(BinaryColumnType::class, DatabaseType.Binary),
            Pair(BlobColumnType::class, DatabaseType.Blob),
            Pair(BooleanColumnType::class, DatabaseType.Boolean),
            Pair(UUIDColumnType::class, DatabaseType.UUID)
    )

    private val kotlinTypeMap: HashMap<KClass<*>, DatabaseType> = hashMapOf(
            Pair(Char::class, DatabaseType.Character),
            Pair(Integer::class, DatabaseType.Integer),
            Pair(Long::class, DatabaseType.Long),
            Pair(Float::class, DatabaseType.Float),
            Pair(Double::class, DatabaseType.Double),
            Pair(Enum::class, DatabaseType.Enumeration),
            Pair(Date::class, DatabaseType.Date),
            Pair(String::class, DatabaseType.Varchar),
            Pair(Boolean::class, DatabaseType.Boolean)
    )

    fun fromExposedType(typeClass: KClass<*>): DatabaseType {
        val result = this.exposedTypeMap[typeClass]
        if (result != null) {
            return result
        } else {
            throw IllegalArgumentException("Unknown type: ${typeClass.qualifiedName}.")
        }
    }

    fun fromKotlinType(typeClass: KClass<*>): DatabaseType {
        val result = this.kotlinTypeMap[typeClass]
        if (result != null) {
            return result
        } else {
            throw IllegalArgumentException("Unknown type: ${typeClass.qualifiedName}.")
        }
    }

    fun fromString(type: String): DatabaseType {
        return DatabaseType.valueOf(type)
    }
}