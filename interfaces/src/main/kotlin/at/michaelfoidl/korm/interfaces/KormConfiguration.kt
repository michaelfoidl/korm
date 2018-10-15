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
     * The package all the generated migrations should be put in. This value is relative to the [rootPackage].
     */
    val migrationPackage: String

    /**
     * The root package that contains the [migrationPackage].
     */
    val rootPackage: String

    /**
     * The directory containing the source files and the [rootPackage].
     */
    val sourceDirectory: String

    /**
     * The directory containing the compiled files. The structure is the same as in [sourceDirectory].
     */
    val buildDirectory: String
}