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

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import kotlin.reflect.KClass

object ElementConverter {
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
        return getPackageName(elementClass.qualifiedName!!)
    }

    fun getPackageName(qualifiedName: String): String {
        return qualifiedName.substringBeforeLast('.')
    }

    fun getSimpleName(qualifiedName: String): String {
        return qualifiedName.substringAfterLast('.')
    }

}