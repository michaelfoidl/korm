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

package at.michaelfoidl.korm.core.test.database

import at.michaelfoidl.korm.core.connection.ConnectionProvider
import at.michaelfoidl.korm.core.database.BaseDatabase
import at.michaelfoidl.korm.core.database.DatabaseState
import at.michaelfoidl.korm.core.runtime.ClassFetcher
import at.michaelfoidl.korm.interfaces.*
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.zaxxer.hikari.HikariConfig
import org.amshove.kluent.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.stubbing.Answer
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.validateMockitoUsage





class BaseDatabaseTests {
    private val kormConfiguration: KormConfiguration = mock()
    private val databaseConfiguration: DatabaseConfiguration = mock()
    private val hikariConfiguration: HikariConfig = mock()
    private val classFetcher: ClassFetcher = mock()
    private val connectionProvider: ConnectionProvider = mock()
    private val databaseState: DatabaseState = mock()

    private val baseDatabaseImplementation = object : BaseDatabase(
            this.kormConfiguration,
            this.databaseConfiguration,
            this.hikariConfiguration,
            this.classFetcher,
            this.connectionProvider,
            this.databaseState
    ) {
        override fun provideHikariConfiguration(configuration: DatabaseConfiguration): HikariConfig {
            return mock()
        }

        fun doMigrate() {
            super.migrate()
        }

        fun doInitializeDatabase() {
            super.initializeDatabase()
        }

    }

    @BeforeEach
    fun setup() {
        When calling this.classFetcher.fetchMigration(any()) itReturns mock()

        When calling this.connectionProvider.provideConnection() itAnswers Answer<DatabaseConnection> {
            val result: DatabaseConnection = mock()



            result
        }

        When calling this.databaseState.isDatabaseInitialized() itReturns true
        When calling this.databaseState.getCurrentVersion() itReturns 2

        When calling this.databaseConfiguration.update(any()) itAnswers Answer<DatabaseConfiguration> {
            val result: DatabaseConfiguration = mock()

            When calling result.databaseVersion itReturns it.getArgument(0)

            result
        }
    }

    @Test
    fun baseDatabase_migrate_shouldDoNothingIfVersionsMatch() {

        // Arrange
        When calling this.databaseConfiguration.databaseVersion itReturns 2

        // Act
        this.baseDatabaseImplementation.doMigrate()

        // Assert
        VerifyNoInteractions on this.classFetcher
    }

    @Test
    fun baseDatabase_migrate_shouldFetchCorrectMigrationsWhenUpgrading() {

        // Arrange
        When calling this.databaseConfiguration.databaseVersion itReturns 3

        // Act
        this.baseDatabaseImplementation.doMigrate()

        // Assert
        argumentCaptor<DatabaseConfiguration>().apply {
            Verify on this@BaseDatabaseTests.classFetcher that this@BaseDatabaseTests.classFetcher.fetchMigration(capture()) was called

            this.allValues.count() shouldEqual 1
            this.lastValue.databaseVersion shouldBe 2
        }
    }

    @Test
    fun baseDatabase_migrate_shouldFetchCorrectMigrationsWhenDowngrading() {

        // Arrange
        When calling this.databaseConfiguration.databaseVersion itReturns 1

        // Act
        this.baseDatabaseImplementation.doMigrate()

        // Assert
        argumentCaptor<DatabaseConfiguration>().apply {
            Verify on this@BaseDatabaseTests.classFetcher that this@BaseDatabaseTests.classFetcher.fetchMigration(capture()) was called

            this.allValues.count() shouldEqual 1
            this.lastValue.databaseVersion shouldBe 1
        }
    }

    @Test
    fun baseDatabase_initializeDatabase_shouldInitializeDatabase() {

        // Act
        this.baseDatabaseImplementation.doInitializeDatabase()

        // Assert
        Verify on this.databaseState that this.databaseState.initializeDatabase() was called
    }
}