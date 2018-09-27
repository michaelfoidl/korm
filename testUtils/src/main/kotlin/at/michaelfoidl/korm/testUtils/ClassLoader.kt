package at.michaelfoidl.korm.testUtils

import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.createInstance

class ClassLoader(
        private val root: File
) {

    val loader = URLClassLoader(
            listOf(root.toURI().toURL()).toTypedArray(),
            this::class.java.classLoader)

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> objectInstance(clazzName: String): T? = loader.loadClass(clazzName).kotlin.objectInstance as T

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> createInstance(clazzName: String): T? = loader.loadClass(clazzName).kotlin.createInstance() as T
}