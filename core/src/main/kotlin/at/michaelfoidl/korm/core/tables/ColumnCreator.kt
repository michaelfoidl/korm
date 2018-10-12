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

package at.michaelfoidl.korm.core.tables

import at.michaelfoidl.korm.annotations.ForeignKey
import at.michaelfoidl.korm.core.io.ElementConverter
import at.michaelfoidl.korm.core.io.KotlinDatabaseTypeConverter
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.exposed.sql.Column
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror


object ColumnCreator {
    fun createColumn(field: Element): PropertySpec {
        val columnClassName = ClassName(ElementConverter.getPackageName(Column::class), Column::class.simpleName!!)

        return if (field.getAnnotation(ForeignKey::class.java) == null) {
            val kotlinFieldType = KotlinDatabaseTypeConverter.convertToKotlinType(ElementConverter.getTypeName(field).toString())
            val fieldClassName = ClassName(ElementConverter.getPackageName(kotlinFieldType), ElementConverter.getSimpleName(kotlinFieldType))
            val parameterizedColumnClassName = columnClassName.parameterizedBy(fieldClassName)
            createSimple(field, parameterizedColumnClassName)
        } else {
            val fieldClassName = ClassName(ElementConverter.getPackageName(Long::class), Long::class.simpleName!!)
            val parameterizedColumnClassName = columnClassName.parameterizedBy(fieldClassName)
            createComplex(field, parameterizedColumnClassName)
        }
    }

    private fun createSimple(field: Element, parameterizedTypeName: ParameterizedTypeName): PropertySpec {
        return PropertySpec.builder(field.simpleName.toString(), parameterizedTypeName)
                .initializer(createInitializer(field))
                .build()
    }

    private fun createComplex(field: Element, parameterizedTypeName: ParameterizedTypeName): PropertySpec {
        val annotation = field.getAnnotation(ForeignKey::class.java)
        var referencedClass: TypeMirror? = null
        try {
            annotation.referencedClass
        } catch (mte: MirroredTypeException) {
            referencedClass = mte.typeMirror
        }

        return PropertySpec.builder(field.simpleName.toString(), parameterizedTypeName)
                .initializer("long(\"" + ElementConverter.getClassName(field) + "\") references " + ElementConverter.getSimpleName(referencedClass!!.asTypeName().toString()) + "Table." + annotation.referencedProperty)
                .build()
    }

    private fun createInitializer(field: Element): String {
        val databaseType = KotlinDatabaseTypeConverter.convertToDatabaseType(ElementConverter.getTypeName(field).toString())
        return if (databaseType == "varchar") {
            databaseType + "(\"" + ElementConverter.getClassName(field) + "\", 255)"
        } else {
            databaseType + "(\"" + ElementConverter.getClassName(field) + "\")"
        }
    }
}