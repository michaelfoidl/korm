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

package at.michaelfoidl.korm.core.test.io

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.ClassTypeWrapper
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

class IOOracleTests {
    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
            migrationPackage = "migrations",
            kormPackage = "at.michaelfoidl.test",
            sourceDirectory = "test/src/main",
            buildDirectory = "build/main",
            rootDirectory = "korm"
    )

    @Test
    fun ioOracle_migrationSourceDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getMigrationBuilder("MyDatabase", 3, this.kormConfiguration).sourcePath()

        // Assert
        result shouldEqual "korm/test/src/main/at/michaelfoidl/test/migrations"
    }

    @Test
    fun ioOracle_migrationBuildDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getMigrationBuilder("MyDatabase", 3, this.kormConfiguration).buildPath()

        // Assert
        result shouldEqual "korm/build/main/at/michaelfoidl/test/migrations"
    }

    @Test
    fun ioOracle_migrationPackage_shouldBeCorrect() {

        // Act
        val result = IOOracle.getMigrationBuilder("MyDatabase", 3, this.kormConfiguration).packageName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.migrations"
    }

    @Test
    fun ioOracle_migrationSimpleName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getMigrationBuilder("MyDatabase", 3, this.kormConfiguration).simpleName()

        // Assert
        result shouldEqual "MyDatabase_Migration_v3_4"
    }

    @Test
    fun ioOracle_migrationQualifiedName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getMigrationBuilder("MyDatabase", 3, this.kormConfiguration).qualifiedName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.migrations.MyDatabase_Migration_v3_4"
    }

    @Test
    fun ioOracle_databaseSourceDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getDatabaseBuilder("MyDatabase", 3, this.kormConfiguration).sourcePath()

        // Assert
        result shouldEqual "korm/build/korm/generatedSrc/at/michaelfoidl/test/database"
    }

    @Test
    fun ioOracle_databaseBuildDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getDatabaseBuilder("MyDatabase", 3, this.kormConfiguration).buildPath()

        // Assert
        result shouldEqual "korm/build/korm/generatedBuild/at/michaelfoidl/test/database"
    }

    @Test
    fun ioOracle_databasePackage_shouldBeCorrect() {

        // Act
        val result = IOOracle.getDatabaseBuilder("MyDatabase", 3, this.kormConfiguration).packageName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.database"
    }

    @Test
    fun ioOracle_databaseSimpleName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getDatabaseBuilder("MyDatabase", 3, this.kormConfiguration).simpleName()

        // Assert
        result shouldEqual "MyDatabase_v3"
    }

    @Test
    fun ioOracle_databaseQualifiedName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getDatabaseBuilder("MyDatabase", 3, this.kormConfiguration).qualifiedName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.database.MyDatabase_v3"
    }

    @Test
    fun ioOracle_tableSourceDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getTableBuilder(ClassTypeWrapper(this::class), this.kormConfiguration).sourcePath()

        // Assert
        result shouldEqual "korm/build/korm/generatedSrc/at/michaelfoidl/test/tables"
    }

    @Test
    fun ioOracle_tableBuildDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getTableBuilder(ClassTypeWrapper(this::class), this.kormConfiguration).buildPath()

        // Assert
        result shouldEqual "korm/build/korm/generatedBuild/at/michaelfoidl/test/tables"
    }

    @Test
    fun ioOracle_tablePackage_shouldBeCorrect() {

        // Act
        val result = IOOracle.getTableBuilder(ClassTypeWrapper(this::class), this.kormConfiguration).packageName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.tables"
    }


    @Test
    fun ioOracle_tableSimpleName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getTableBuilder(ClassTypeWrapper(this::class), this.kormConfiguration).simpleName()

        // Assert
        result shouldEqual "IOOracleTestsTable"
    }

    @Test
    fun ioOracle_tableQualifiedName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getTableBuilder(ClassTypeWrapper(this::class), this.kormConfiguration).qualifiedName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.tables.IOOracleTestsTable"
    }

    @Test
    fun ioOracle_configurationSourceDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getKormConfigurationBuilder(this.kormConfiguration).sourcePath()

        // Assert
        result shouldEqual "korm/build/korm/generatedSrc"
    }

    @Test
    fun ioOracle_configurationBuildDirectory_shouldBeCorrect() {

        // Act
        val result = IOOracle.getKormConfigurationBuilder(this.kormConfiguration).buildPath()

        // Assert
        result shouldEqual "korm/build/korm/generatedBuild"
    }

    @Test
    fun ioOracle_configurationPackage_shouldBeCorrect() {

        // Act
        val result = IOOracle.getKormConfigurationBuilder(this.kormConfiguration).packageName()

        // Assert
        result shouldEqual ""
    }

    @Test
    fun ioOracle_configurationSimpleName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getKormConfigurationBuilder(this.kormConfiguration).simpleName()

        // Assert
        result shouldEqual "CurrentKormConfiguration"
    }

    @Test
    fun ioOracle_configurationQualifiedName_shouldBeCorrect() {

        // Act
        val result = IOOracle.getKormConfigurationBuilder(this.kormConfiguration).qualifiedName()

        // Assert
        result shouldEqual "CurrentKormConfiguration"
    }
}