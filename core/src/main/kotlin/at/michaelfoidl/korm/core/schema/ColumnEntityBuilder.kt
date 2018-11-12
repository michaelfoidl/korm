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
import at.michaelfoidl.korm.core.exposed.TypeAdapter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

internal open class ColumnEntityBuilder protected constructor(
        protected val propertyName: String,
        protected val propertyReturnType: KClass<*>,
        protected val isPropertyNullable: Boolean,
        protected val columnNameAnnotation: ColumnName?,
        protected val autoIncrementAnnotation: AutoIncrement?,
        protected val foreignKeyAnnotation: at.michaelfoidl.korm.annotations.ForeignKey?,
        protected val primaryKeyAnnotation: at.michaelfoidl.korm.annotations.PrimaryKey?,
        protected val indexedAnnotation: Indexed?
) : ColumnBuilder {

    constructor(property: KProperty<*>) : this(
            property.name,
            property.returnType.jvmErasure,
            property.returnType.isMarkedNullable,
            property.javaField!!.getAnnotation(ColumnName::class.java),
            property.javaField!!.getAnnotation(AutoIncrement::class.java),
            property.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
            property.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
            property.javaField!!.getAnnotation(Indexed::class.java)
    )


    override fun getName(): String {
        return if (this.columnNameAnnotation?.columnName == null || this.columnNameAnnotation.columnName.isBlank()) {
            this.propertyName
        } else {
            this.columnNameAnnotation.columnName
        }
    }

    override fun getType(): DatabaseType {
        return TypeAdapter.fromKotlinType(this.propertyReturnType)
    }

    override fun isNullable(): Boolean {
        return this.isPropertyNullable && !this.isPrimaryKey
    }

    override fun isAutoIncrement(): Boolean {
        return autoIncrementAnnotation != null || this.isPrimaryKey && this.primaryKeyAnnotation!!.autoIncrement
    }

    override fun isIndexed(): Boolean {
        return this.indexedAnnotation != null || this.isPrimaryKey || this.isForeignKey
    }

    override val isForeignKey: Boolean
        get() = this.foreignKeyAnnotation != null

    override val isPrimaryKey: Boolean
        get() = this.primaryKeyAnnotation != null

    override fun asForeignKeyBuilder(): ForeignKeyEntityBuilder {
        if (!this.isForeignKey) {
            throw IllegalStateException("Property '{${this.propertyName}' does not represent a foreign key. Use the ${at.michaelfoidl.korm.annotations.ForeignKey::class} annotation.")
        }
        return ForeignKeyEntityBuilder(
                this.propertyName,
                this.propertyReturnType,
                this.isPropertyNullable,
                this.columnNameAnnotation,
                this.autoIncrementAnnotation,
                this.foreignKeyAnnotation,
                this.primaryKeyAnnotation,
                this.indexedAnnotation)
    }

    override fun asPrimaryKeyBuilder(): PrimaryKeyEntityBuilder {
        if (!this.isPrimaryKey) {
            throw IllegalStateException("Property '{${this.propertyName}' does not represent a primary key. Use the ${at.michaelfoidl.korm.annotations.PrimaryKey::class} annotation.")
        }
        return PrimaryKeyEntityBuilder(
                this.propertyName,
                this.propertyReturnType,
                this.isPropertyNullable,
                this.columnNameAnnotation,
                this.autoIncrementAnnotation,
                this.foreignKeyAnnotation,
                this.primaryKeyAnnotation,
                this.indexedAnnotation)
    }
}