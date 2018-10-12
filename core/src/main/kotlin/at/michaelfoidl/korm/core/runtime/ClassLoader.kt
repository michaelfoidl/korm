package at.michaelfoidl.korm.core.runtime

import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.createInstance

internal class ClassLoader(
        private val root: File
) {

    private val loader = URLClassLoader(
            listOf(this.root.toURI().toURL()).toTypedArray(),
            this::class.java.classLoader)

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> objectInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.objectInstance as T
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> createInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.createInstance() as T
    }
}