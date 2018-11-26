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

package at.michaelfoidl.korm.core.connection

import at.michaelfoidl.korm.core.exceptions.ExecutionException
import at.michaelfoidl.korm.core.exposed.ExposedAdapter
import at.michaelfoidl.korm.core.exposed.TransactionLevel
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import com.zaxxer.hikari.HikariDataSource

class DefaultDatabaseConnection internal constructor(
        private val exposedAdapter: ExposedAdapter = ExposedAdapter
) : DatabaseConnection {

    constructor() : this(ExposedAdapter)

    private lateinit var dataSource: HikariDataSource
    override var isOpen: Boolean = false

    override fun open(dataSource: HikariDataSource): DatabaseConnection {
        this.dataSource = dataSource
        this.exposedAdapter.connect(this.dataSource)
        this.isOpen = true
        return this
    }

    override fun close() {
        this.isOpen = false
    }

    override fun executeInTransaction(action: () -> Unit) {
        execute {
            try {
                this.exposedAdapter.transaction(TransactionLevel.TRANSACTION_READ_UNCOMMITTED, 1) {
                    action()
                }
            } catch (e: Exception) {
                throw ExecutionException("Could not execute action.", e)
            }
        }
    }

    private fun ensureThatConnectionIsValid() {
        if (!this.isOpen) {
            throw IllegalStateException("DefaultDatabaseConnection must be opened to use it.")
        }
    }

    private fun execute(action: () -> Unit) {
        ensureThatConnectionIsValid()
        action()
        close()
    }
}