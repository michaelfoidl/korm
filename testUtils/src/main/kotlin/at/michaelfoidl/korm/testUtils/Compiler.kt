package at.michaelfoidl.korm.testUtils

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File

object Compiler {
    fun execute(input: File, output: File, vararg additionalClassPathEntries: String): Boolean {
        return K2JVMCompiler().run {
            val args = K2JVMCompilerArguments().apply {
                freeArgs = listOf(input.absolutePath)
                destination = output.absolutePath
                classpath = System.getProperty("java.class.path")
                        .split(File.pathSeparator)
                        .union(additionalClassPathEntries.toList())
                        .asSequence()
                        .filter { File(it).exists() && File(it).canRead() }
                        .joinToString(File.pathSeparator)
                noStdlib = true
            }
            output.deleteOnExit()
            execImpl(
                    PrintingMessageCollector(
                            System.out,
                            MessageRenderer.WITHOUT_PATHS, true),
                    Services.EMPTY,
                    args)
        }.code == 0
    }
}