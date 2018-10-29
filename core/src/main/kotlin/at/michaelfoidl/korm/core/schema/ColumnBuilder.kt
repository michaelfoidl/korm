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

package at.michaelfoidl.korm.core.schema

import at.michaelfoidl.korm.annotations.AutoIncrement
import at.michaelfoidl.korm.annotations.ColumnName
import at.michaelfoidl.korm.annotations.Indexed
import at.michaelfoidl.korm.annotations.Nullable
import at.michaelfoidl.korm.core.exposed.TypeAdapter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

internal open class ColumnBuilder protected constructor(
        protected val propertyName: String,
        protected val propertyReturnType: KClass<*>,
        protected val columnNameAnnotation: ColumnName?,
        protected val nullableAnnotation: Nullable?,
        protected val autoIncrementAnnotation: AutoIncrement?,
        protected val foreignKeyAnnotation: at.michaelfoidl.korm.annotations.ForeignKey?,
        protected val primaryKeyAnnotation: at.michaelfoidl.korm.annotations.PrimaryKey?,
        protected val indexedAnnotation: Indexed?
) {

    constructor(property: KProperty<*>) : this(
            property.name,
            property.returnType.jvmErasure,
            property.javaField!!.getAnnotation(ColumnName::class.java),
            property.javaField!!.getAnnotation(Nullable::class.java),
            property.javaField!!.getAnnotation(AutoIncrement::class.java),
            property.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
            property.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
            property.javaField!!.getAnnotation(Indexed::class.java)
    )

    fun getName(): String {
        return if (this.columnNameAnnotation?.columnName == null || this.columnNameAnnotation.columnName.isBlank()) {
            this.propertyName
        } else {
            this.columnNameAnnotation.columnName
        }
    }

    fun getType(): DatabaseType {
        return TypeAdapter.fromKotlinType(this.propertyReturnType)
    }

    fun isNullable(): Boolean {
        return nullableAnnotation != null || isPrimaryKey()
    }

    fun isAutoIncrement(): Boolean {
        return autoIncrementAnnotation != null || (isPrimaryKey() && this.primaryKeyAnnotation!!.autoIncrement)
    }

    fun isIndexed(): Boolean {
        return this.indexedAnnotation != null || isPrimaryKey() || isForeignKey()
    }

    fun isForeignKey(): Boolean {
        return this.foreignKeyAnnotation != null
    }

    fun isPrimaryKey(): Boolean {
        return this.primaryKeyAnnotation != null
    }

    fun toColumn(): Column {
        return Column(
                getName(),
                getType(),
                isNullable(),
                isAutoIncrement(),
                isIndexed())
    }

    fun asForeignKeyBuilder(): ForeignKeyBuilder {
        if (!this.isForeignKey()) {
            throw IllegalStateException("Property '{${this.propertyName}' does not represent a foreign key. Use the ${at.michaelfoidl.korm.annotations.ForeignKey::class} annotation.")
        }
        return ForeignKeyBuilder(
                this.propertyName,
                this.propertyReturnType,
                this.columnNameAnnotation,
                this.nullableAnnotation,
                this.autoIncrementAnnotation,
                this.foreignKeyAnnotation,
                this.primaryKeyAnnotation,
                this.indexedAnnotation)
    }

    fun asPrimaryKeyBuilder(): PrimaryKeyBuilder {
        if (!this.isPrimaryKey()) {
            throw IllegalStateException("Property '{${this.propertyName}' does not represent a primary key. Use the ${at.michaelfoidl.korm.annotations.PrimaryKey::class} annotation.")
        }
        return PrimaryKeyBuilder(
                this.propertyName,
                this.propertyReturnType,
                this.columnNameAnnotation,
                this.nullableAnnotation,
                this.autoIncrementAnnotation,
                this.foreignKeyAnnotation,
                this.primaryKeyAnnotation,
                this.indexedAnnotation)
    }
}