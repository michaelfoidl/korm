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

package at.michaelfoidl.korm.testUtils

import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.createInstance

class ClassLoader(
        private val root: File
) {
    val loader = URLClassLoader(
            listOf(this.root.toURI().toURL()).toTypedArray(),
            this::class.java.classLoader)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> objectInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.objectInstance as T
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> createInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.createInstance() as T
    }
}