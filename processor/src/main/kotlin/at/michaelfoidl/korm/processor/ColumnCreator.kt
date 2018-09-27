package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.ForeignKey
import at.michaelfoidl.korm.processor.ProcessingUtils.getClassName
import at.michaelfoidl.korm.processor.ProcessingUtils.getPackageName
import at.michaelfoidl.korm.processor.ProcessingUtils.getSimpleName
import at.michaelfoidl.korm.processor.ProcessingUtils.getTypeName
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
        val columnClassName = ClassName(getPackageName(Column::class), Column::class.simpleName!!)

        return if (field.getAnnotation(ForeignKey::class.java) == null) {
            val kotlinFieldType = TypeConverter.convertToKotlinType(getTypeName(field).toString())
            val fieldClassName = ClassName(getPackageName(kotlinFieldType), getSimpleName(kotlinFieldType))
            val parameterizedColumnClassName = columnClassName.parameterizedBy(fieldClassName)
            createSimple(field, parameterizedColumnClassName)
        } else {
            val fieldClassName = ClassName(getPackageName(Long::class), Long::class.simpleName!!)
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
                .initializer("long(\"" + getClassName(field) + "\") references " + getSimpleName(referencedClass!!.asTypeName().toString()) + "Table." + annotation.referencedProperty)
                .build()
    }

    private fun createInitializer(field: Element): String {
        val databaseType = TypeConverter.convertToDatabaseType(getTypeName(field).toString())
        return if (databaseType == "varchar") {
            databaseType + "(\"" + getClassName(field) + "\", 255)"
        } else {
            databaseType + "(\"" + getClassName(field) + "\")"
        }
    }
}