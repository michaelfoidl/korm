package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.processor.ProcessingUtils.getClassName
import at.michaelfoidl.korm.processor.ProcessingUtils.getPackageName
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import org.jetbrains.exposed.sql.Table
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter

@AutoService(Processor::class)
@SupportedOptions(EntityProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Entity::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (!roundEnv!!.processingOver()) {
            val file = FileSpec.builder(getPackageName("test"), "Database")
            roundEnv
                    .getElementsAnnotatedWith(Entity::class.java)
                    .forEach {
                        file.addType(generateTableDefinitionFor(it))
                    }

            val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            file.build().writeTo(File(kaptKotlinGeneratedDir, ""))
        }
        return true
    }

    private fun generateTableDefinitionFor(element: Element): TypeSpec {
        val className = getClassName(element)
        val fileName = "${className}Table"
        val typeBuilder = TypeSpec.objectBuilder(fileName)
                .superclass(Table::class.asTypeName())

        typeBuilder.addProperties(generateColumnDefinitionsFor(element))

        return typeBuilder.build()

    }

    private fun generateColumnDefinitionsFor(element: Element): Collection<PropertySpec> {
        val results: MutableCollection<PropertySpec> = ArrayList()

        ElementFilter.fieldsIn(element.enclosedElements).forEach { field ->
            results.add(ColumnCreator.createColumn(field))
        }

        return results
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    /*
    DEBUG COMMAND:
    gradlew --no-daemon clean example:kaptKotlin -Dkotlin.daemon.jvm.options="-Xdebug,-Xrunjdwp:transport=dt_socket\,address=5005\,server=y\,suspend=y"
     */
}