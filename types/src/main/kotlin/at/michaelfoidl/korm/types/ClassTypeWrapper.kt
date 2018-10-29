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

import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

class ClassTypeWrapper(
        wrapped: KClass<*>,
        private val givenName: String? = null
) : BaseTypeWrapper<KClass<*>>(wrapped) {
    override fun <T : Annotation> hasAnnotation(annotationClass: KClass<T>): Boolean {
        return this.wrapped.annotations.any { it::class.qualifiedName == annotationClass.qualifiedName }
    }

    override fun <T : Annotation> getAnnotation(annotationClass: KClass<T>): T? {
        @Suppress("UNCHECKED_CAST")
        return this.wrapped.annotations.find { it::class.qualifiedName == annotationClass.qualifiedName } as T?
    }

    override val type: TypeWrapperType = TypeWrapperType.Class

    override val typeName: String? = this.wrapped.qualifiedName

    override val name: String? = this.givenName ?: this.wrapped.simpleName

    override val fields: Lazy<List<TypeWrapper>> = lazy { this.wrapped.memberProperties.map { ClassTypeWrapper(it.returnType.jvmErasure, it.name) } }

    override val methods: Lazy<List<TypeWrapper>> = lazy { this.wrapped.memberFunctions.map { ClassTypeWrapper(it.returnType.jvmErasure, it.name) } }
}