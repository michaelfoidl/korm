package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.processor.ProcessingUtils.getClassName
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import org.jetbrains.exposed.sql.Column
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
        roundEnv!!
                .getElementsAnnotatedWith(Entity::class.java)
                .forEach {
                    val pack = processingEnv.elementUtils.getPackageOf(it).toString()
                    generateFile(it, pack)
                }
        return true
    }

    private fun generateFile(element: Element, pack: String) {
        val className = getClassName(element)
        val fileName = "korm_$className"
        val file = FileSpec.builder(pack, fileName)
                .addType(generateType(element))
                .build()

        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir, "$fileName.kt"))
    }

    private fun generateType(element: Element): TypeSpec {
        val className = getClassName(element)
        val fileName = "${className}Table"
        val typeBuilder = TypeSpec.objectBuilder(fileName)
                .addSuperinterface(Table::class.asTypeName())

        typeBuilder.addProperties(generateLocalProperties(element))

        return typeBuilder.build()

    }

    private fun generateLocalProperties(element: Element): Collection<PropertySpec> {
        val results: MutableCollection<PropertySpec> = ArrayList()

        ElementFilter.fieldsIn(element.enclosedElements).forEach { field ->
            val propertyBuilder = PropertySpec.builder(field.simpleName.toString(), Column::class)
                    .initializer(field.asType().asTypeName().toString())

            results.add(propertyBuilder.build())
        }

        return results
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}