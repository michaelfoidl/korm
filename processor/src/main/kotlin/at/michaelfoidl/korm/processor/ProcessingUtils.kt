package at.michaelfoidl.korm.processor

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import kotlin.reflect.KClass

object ProcessingUtils {
    fun getClassName(element: Element): String {
        return element.simpleName.toString()
    }

    fun getTypeName(element: Element): TypeName {
        return element.asType().asTypeName()
    }

    fun getPackageName(element: Element): String {
        return getTypeName(element).toString().substringBeforeLast('.')
    }

    fun getPackageName(elementClass: KClass<*>): String {
        return elementClass.qualifiedName!!.substringBeforeLast('.')
    }

    fun getPackageName(qualifiedName: String): String {
        return qualifiedName.substringBeforeLast('.')
    }

    fun getSimpleName(qualifiedName: String): String {
        return qualifiedName.substringAfterLast('.')
    }

}