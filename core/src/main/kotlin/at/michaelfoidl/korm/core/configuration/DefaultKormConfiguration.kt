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
}