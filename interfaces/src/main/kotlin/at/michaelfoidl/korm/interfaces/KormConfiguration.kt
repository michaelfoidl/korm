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

package at.michaelfoidl.korm.interfaces

/**
 * The configuration of the OR-Mapper.
 *
 * @since 0.2
 */
interface KormConfiguration {

    /**
     * The package all the generated migrations should be put in. This value is relative to the [kormPackage].
     */
    val migrationPackage: String

    /**
     * The package all generated database files are stored in. This value is relative to the [kormPackage]. Note that
     * the database files are generated every time the project is built and therefore should not be checked in into your
     * version control system.
     */
    val databasePackage: String

    /**
     * The package all generated table files are stored in. This value is relative to the [kormPackage]. Note that
     * the table files are generated every time the project is built and therefore should not be checked in into your
     * version control system.
     */
    val tablePackage: String

    /**
     * The root package that contains [migrationPackage], [databasePackage] and [tablePackage].
     */
    val kormPackage: String

    /**
     * The directory containing the source files. This value is relative to [rootDirectory].
     */
    val sourceDirectory: String

    /**
     * The directory containing the compiled files. This value is relative to [rootDirectory].
     */
    val buildDirectory: String

    /**
     * The directory containing the generated source files. This value is relative to [rootDirectory].
     */
    val generatedSourceDirectory: String

    /**
     * The directory containing the compiled generated files. This value is relative to [rootDirectory].
     */
    val generatedBuildDirectory: String

    /**
     * The root directory of the project.
     */
    val rootDirectory: String
}