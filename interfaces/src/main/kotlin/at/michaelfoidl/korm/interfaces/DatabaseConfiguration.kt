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
 * The configuration of the Database.
 *
 * @since 0.2
 */
interface DatabaseConfiguration {

    /**
     * The unique name of the database. Changing the name may cause problems with existing migrations for this database since
     * they can not be associated with it anymore.
     */
    val databaseName: String

    /**
     * The version of the database. Always increase the version by one if you make changes to your entity models and add
     * a migration.
     */
    val databaseVersion: Long

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
     * Updates the version of the configuration.
     *
     * @param databaseVersion the new version of the configuration
     * @return a new updated configuration instance
     */
    fun update(databaseVersion: Long): DatabaseConfiguration
}