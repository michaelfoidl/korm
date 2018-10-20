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

package at.michaelfoidl.korm.processor

import at.michaelfoidl.korm.annotations.Database
import at.michaelfoidl.korm.core.configuration.ConfigurationCreator
import at.michaelfoidl.korm.core.configuration.DefaultDatabaseConfiguration
import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.database.DatabaseCreator
import at.michaelfoidl.korm.interfaces.DatabaseConfiguration
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.ElementTypeWrapper
import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.lang.model.element.Element

@AutoService(Processor::class)
class DatabaseProcessor : BaseProcessor(Database::class.java) {
    override fun provideSupportedOptions(): MutableSet<String> {
        return mutableSetOf(
                KAPT_KORM_DATABASE_NAME_OPTION_NAME,
                KAPT_KORM_DATABASE_VERSION_OPTION_NAME,
                KAPT_KORM_DATABASE_PATH_OPTION_NAME,
                KAPT_KORM_USERNAME_OPTION_NAME,
                KAPT_KORM_PASSWORD_OPTION_NAME)
    }

    override fun doProcess(element: Element?) {
        val databaseConfiguration: DatabaseConfiguration = DefaultDatabaseConfiguration(
                databaseName = processingEnv.options[KAPT_KORM_DATABASE_NAME_OPTION_NAME] ?: "",
                databaseVersion = processingEnv.options[KAPT_KORM_DATABASE_VERSION_OPTION_NAME]?.toLongOrNull() ?: 1,
                databasePath = processingEnv.options[KAPT_KORM_DATABASE_PATH_OPTION_NAME],
                username = processingEnv.options[KAPT_KORM_USERNAME_OPTION_NAME] ?: "",
                password = processingEnv.options[KAPT_KORM_PASSWORD_OPTION_NAME] ?: ""
        )
        val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = processingEnv.options[KAPT_KORM_MIGRATION_PACKAGE_OPTION_NAME] ?: "",
                kormPackage = processingEnv.options[KAPT_KORM_KORM_PACKAGE_OPTION_NAME] ?: "",
                sourceDirectory = processingEnv.options[KAPT_KORM_SOURCE_DIRECTORY_OPTION_NAME] ?: "",
                buildDirectory = processingEnv.options[KAPT_KORM_BUILD_DIRECTORY_OPTION_NAME] ?: "",
                rootDirectory = processingEnv.options[KAPT_KORM_ROOT_DIRECTORY_OPTION_NAME] ?: ""
        )
        ConfigurationCreator().createKormConfiguration(kormConfiguration)
        DatabaseCreator(databaseConfiguration, kormConfiguration).createDatabase(ElementTypeWrapper(element!!))
    }

    companion object {
        const val KAPT_KORM_DATABASE_NAME_OPTION_NAME: String = "kapt.korm.databaseName"
        const val KAPT_KORM_DATABASE_VERSION_OPTION_NAME: String = "kapt.korm.databaseVersion"
        const val KAPT_KORM_DATABASE_PATH_OPTION_NAME: String = "kapt.korm.databasePath"
        const val KAPT_KORM_USERNAME_OPTION_NAME: String = "kapt.korm.username"
        const val KAPT_KORM_PASSWORD_OPTION_NAME: String = "kapt.korm.password"
    }

    /*
    DEBUG COMMAND:
    gradlew --no-daemon clean integrationTests:kaptKotlin -Dkotlin.daemon.jvm.options="-Xdebug,-Xrunjdwp:transport=dt_socket\,address=5005\,server=y\,suspend=y"
     */
}