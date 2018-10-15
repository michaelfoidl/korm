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

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

fun TypeWrapper.asTypeName(): TypeName {
    return when(this.type) {
        TypeWrapperType.TypeMirror -> {
            (this.wrapped as TypeMirror).asTypeName()
        }
        TypeWrapperType.Class -> {
            (this.wrapped as KClass<*>).asTypeName()
        }
    }
}