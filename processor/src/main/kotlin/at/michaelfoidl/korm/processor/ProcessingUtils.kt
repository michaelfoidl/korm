package at.michaelfoidl.korm.processor

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element

object ProcessingUtils {
    fun getClassName(element: Element): String {
        return element.simpleName.toString()
    }

    fun getTypeName(element: Element): TypeName {
        return element.asType().asTypeName()
    }
}