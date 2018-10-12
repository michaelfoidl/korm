package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.core.tables.TableGenerator
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedOptions(
        EntityProcessor.KAPT_KORM_ROOT_PACKAGE_OPTION_NAME,
        EntityProcessor.KAPT_KORM_ROOT_DIRECTORY_OPTION_NAME)
class EntityProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Entity::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (!roundEnv!!.processingOver()) {
            roundEnv
                    .getElementsAnnotatedWith(Entity::class.java)
                    .forEach {
                        TableGenerator(
                                processingEnv.options[KAPT_KORM_ROOT_PACKAGE_OPTION_NAME] ?: "",
                                processingEnv.options[KAPT_KORM_ROOT_DIRECTORY_OPTION_NAME] ?: ""
                        ).createTable(it)
                    }
        }
        return true
    }

    companion object {
        const val KAPT_KORM_ROOT_PACKAGE_OPTION_NAME: String = "kapt.korm.rootPackage"
        const val KAPT_KORM_ROOT_DIRECTORY_OPTION_NAME: String = "kapt.korm.rootDirectory"
    }

    /*
    DEBUG COMMAND:
    gradlew --no-daemon clean integrationTests:kaptKotlin -Dkotlin.daemon.jvm.options="-Xdebug,-Xrunjdwp:transport=dt_socket\,address=5005\,server=y\,suspend=y"
     */
}