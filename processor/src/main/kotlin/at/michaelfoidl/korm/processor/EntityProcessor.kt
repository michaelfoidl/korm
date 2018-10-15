package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.Entity
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.tables.TableCreator
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.ElementTypeWrapper
import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.lang.model.element.Element

@AutoService(Processor::class)
class EntityProcessor : BaseProcessor(Entity::class.java) {
    override fun doProcess(element: Element?) {
        val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = processingEnv.options[KAPT_KORM_MIGRATION_PACKAGE_OPTION_NAME] ?: "",
                rootPackage = processingEnv.options[KAPT_KORM_ROOT_PACKAGE_OPTION_NAME] ?: "",
                sourceDirectory = processingEnv.options[KAPT_KORM_SOURCE_DIRECTORY_OPTION_NAME] ?: ""
        )
        TableCreator(kormConfiguration).createTable(ElementTypeWrapper(element!!))
    }

    override fun provideSupportedOptions(): MutableSet<String> {
        return mutableSetOf()
    }

    /*
    DEBUG COMMAND:
    gradlew --no-daemon clean integrationTests:kaptKotlin -Dkotlin.daemon.jvm.options="-Xdebug,-Xrunjdwp:transport=dt_socket\,address=5005\,server=y\,suspend=y"
     */
}