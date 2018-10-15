package at.michaelfoidl.korm.core.runtime

import java.io.File
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.full.createInstance

internal class ClassLoader(
        private val source: File,
        private val isDirectory: Boolean
) {

    private val loader = URLClassLoader(
            listOf(generateURLFromFile(this.source.absoluteFile, this.isDirectory)).toTypedArray(),
            this::class.java.classLoader)

    private fun generateURLFromFile(source: File, isDirectory: Boolean): URL {
        val directoryIndicator = if (isDirectory) "/" else ""
        return URL("file:///$source$directoryIndicator")
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> objectInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.objectInstance as T
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> createInstance(clazzName: String): T? {
        return loader.loadClass(clazzName).kotlin.createInstance() as T
    }
}