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

import com.zaxxer.hikari.HikariDataSource


/**
 * Represents a connection to a database you can execute statements on.
 *
 * @since 0.1
 */
interface DatabaseConnection {

    /**
     * Initializes the connection with the given data source. The connection has to be initialized before using it.
     *
     * @param dataSource the data source to which the connection should be established.
     */
    fun open(dataSource: HikariDataSource): DatabaseConnection

    /**
     * Closes the connection.
     */
    fun close()

    /**
     * Executes the defined action in a transaction on the database.
     *
     * @param action the action to be executed in a transaction.
     */
    fun executeInTransaction(action: () -> Unit)

    /**
     * Indicates whether the connection is open and therefore ready-to-use.
     */
    var isOpen: Boolean
}