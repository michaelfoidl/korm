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

package at.michaelfoidl.korm.types

import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.util.ElementFilter
import kotlin.reflect.KClass

class ElementTypeWrapper(
        wrapped: Element
) : BaseTypeWrapper<Element>(wrapped) {
    override fun <T : Annotation> hasAnnotation(annotationClass: KClass<T>): Boolean {
        return this.wrapped.getAnnotation(annotationClass.java) != null
    }

    override fun <T : Annotation> getAnnotation(annotationClass: KClass<T>): T? {
        return this.wrapped.getAnnotation(annotationClass.java)
    }

    override val type: TypeWrapperType = TypeWrapperType.TypeMirror

    override val typeName: String? = this.wrapped.asType().asTypeName().toString()

    override val name: String? = this.wrapped.simpleName.toString()

    override val fields: Lazy<List<TypeWrapper>> = lazy { ElementFilter.fieldsIn(this.wrapped.enclosedElements).map { ElementTypeWrapper(it) } }

    override val methods: Lazy<List<TypeWrapper>> = lazy { ElementFilter.methodsIn(this.wrapped.enclosedElements).map { ElementTypeWrapper(it) } }
}