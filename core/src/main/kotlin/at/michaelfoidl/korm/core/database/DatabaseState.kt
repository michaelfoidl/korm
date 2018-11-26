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

package at.michaelfoidl.korm.core.database

import at.michaelfoidl.korm.core.connection.ConnectionProvider
import at.michaelfoidl.korm.core.migrations.InitialMigration
import at.michaelfoidl.korm.core.tables.MasterTable
import org.jetbrains.exposed.sql.*

class DatabaseState(
        private val connectionProvider: ConnectionProvider
) {
    fun getCurrentVersion(): Long {
        var version: Long = -1
        this.connectionProvider.provideConnection().executeInTransaction {
            val maxVersion = MasterTable.version.max().alias("maxVersion")
            version = MasterTable
                    .slice(maxVersion)
                    .selectAll()
                    .first()[maxVersion]!!
        }
        return version
    }

    fun updateVersion(version: Long) {
        TODO("implement")
    }

    fun initializeDatabase() {
        InitialMigration().up(this.connectionProvider.provideConnection())
    }

    fun isDatabaseInitialized(): Boolean {
        var result = false
        this.connectionProvider.provideConnection().executeInTransaction {
            result = MasterTable.exists()
        }
        return result
    }
}