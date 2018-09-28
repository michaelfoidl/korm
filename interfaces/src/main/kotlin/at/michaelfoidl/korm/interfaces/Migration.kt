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
 * Represents a migration of the database schema.
 *
 * @since 0.1.1
 */
interface Migration {

    /**
     * Applies the migration to the database using the given connection.
     *
     * @param connection the connection to use.
     */
    fun up(connection: DatabaseConnection)

    /**
     * Reverts the migration (applied to the database before) using the given connection.
     *
     * @param connection the connection to use.
     */
    fun down(connection: DatabaseConnection)
}