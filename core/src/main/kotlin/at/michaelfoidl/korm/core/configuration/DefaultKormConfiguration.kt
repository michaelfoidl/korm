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

package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.interfaces.KormConfiguration
import java.util.*

open class DefaultKormConfiguration(
        override val migrationPackage: String = "migrations",
        override val kormPackage: String = "",
        override val sourceDirectory: String = "src/main",
        override val buildDirectory: String = "build/classes/kotlin/main",
        override val rootDirectory: String = ""
) : KormConfiguration {
    override val databasePackage: String = "database"
    override val tablePackage: String = "tables"
    override val generatedSourceDirectory: String = "build/korm/generatedSrc"
    override val generatedBuildDirectory: String = "build/korm/generatedBuild"

    companion object {
        private const val MIGRATION_PROPERTY_NAME: String = "at.michaelfoidl.korm.migrationPackage"
        private const val KORMPACKAGE_PROPERTY_NAME: String = "at.michaelfoidl.korm.kormPackage"
        private const val SOURCEDIRECTORY_PROPERTY_NAME: String = "at.michaelfoidl.korm.srcDir"
        private const val BUILDDIRECTORY_PROPERTY_NAME: String = "at.michaelfoidl.korm.buildDir"
        private const val ROOTDIRECTORY_PROPERTY_NAME: String = "at.michaelfoidl.korm.rootDir"

        fun fromProperties(properties: Properties): KormConfiguration {
            return DefaultKormConfiguration(
                    migrationPackage = properties[MIGRATION_PROPERTY_NAME].toString(),
                    kormPackage = properties[KORMPACKAGE_PROPERTY_NAME].toString(),
                    sourceDirectory = properties[SOURCEDIRECTORY_PROPERTY_NAME].toString(),
                    buildDirectory = properties[BUILDDIRECTORY_PROPERTY_NAME].toString(),
                    rootDirectory = properties[ROOTDIRECTORY_PROPERTY_NAME].toString()
            )
        }
    }
}