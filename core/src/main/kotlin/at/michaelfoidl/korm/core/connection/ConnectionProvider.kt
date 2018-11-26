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

import at.michaelfoidl.korm.core.lazy.Cached
import at.michaelfoidl.korm.interfaces.DatabaseConnection
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class ConnectionProvider(
        private var configuration: HikariConfig
) {
    private var connection: Cached<DatabaseConnection> = Cached {
        DefaultDatabaseConnection().open(HikariDataSource(this.configuration))
    }

    fun provideConnection(): DatabaseConnection {
        if (!this.connection.value.isOpen) {
            this.connection.invalidate()
        }
        return this.connection.value
    }

    fun configure(configuration: HikariConfig): ConnectionProvider {
        this.configuration = configuration
        this.connection.invalidate()
        return this
    }
}