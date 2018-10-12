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

import kotlin.reflect.KClass

/**
 * The configuration of the OR-Mapper.
 *
 * @since 0.1
 */
interface KormConfiguration {

    /**
     * The type of the database.
     */
    val databaseType: DatabaseType

    /**
     * The version of the database. Always increase the version by one if you make changes to your entity models and add
     * a migration.
     */
    val databaseVersion: Long

    /**
     * The interface defining the database.
     */
    val databaseInterface: KClass<out Database>

    /**
     * The unique name of the database. Changing the name may cause problems with existing migrations for this database since
     * they can not be associated with it anymore.
     */
    val databaseName: String

    /**
     * The path to the database file. This value is ignored if the database type is an in-memory-database.
     */
    val databasePath: String?

    /**
     * The username used for authentication at the database.
     */
    val username: String

    /**
     * The password used for authentication at the database.
     */
    val password: String

    /**
     * The package all the generated migrations should be put in. This value is relative to the [rootPackage].
     */
    val migrationPackage: String

    /**
     * The package the generated concrete database implementation should be put in. This value is relative to the
     * [rootPackage].
     */
    val databasePackage: String

    /**
     * The root package that contains the [migrationPackage] and the [databasePackage].
     */
    val rootPackage: String

    /**
     * The directory corresponding to the [rootPackage]. Usually, these values should be the same, just with slashes (/)
     * instead of dots (.).
     */
    val rootDirectory: String
}