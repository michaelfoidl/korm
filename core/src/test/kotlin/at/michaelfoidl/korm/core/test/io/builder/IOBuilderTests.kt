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

package at.michaelfoidl.korm.core.test.io.builder

import at.michaelfoidl.korm.core.configuration.DefaultKormConfiguration
import at.michaelfoidl.korm.core.io.builder.IOBuilder
import at.michaelfoidl.korm.interfaces.KormConfiguration
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

class IOBuilderTests {
    private val kormConfiguration: KormConfiguration = DefaultKormConfiguration(
            migrationPackage = "migrations",
            kormPackage = "at.michaelfoidl.test",
            sourceDirectory = "test/src/main",
            buildDirectory = "build/main",
            rootDirectory = "path/to/my/project"
    )

    @Test
    fun ioBuilder_sourcePath_shouldReturnPathOfSourceFile() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath()

        // Assert
        result shouldEqual "path/to/my/project/test/src/main/at/michaelfoidl/test/migrations"
    }

    @Test
    fun ioBuilder_sourcePathWithRoot_shouldReturnPathOfRootDirectory() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath(true)

        // Assert
        result shouldEqual "path/to/my/project/test/src/main"
    }

    @Test
    fun ioBuilder_sourcePathExecutedTwice_shouldReturnSameResult() {

        // Arrange
        val builder = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration")

        // Act
        val result1 = builder.sourcePath()
        val result2 = builder.sourcePath()

        // Assert
        result1 shouldEqual result2
    }

    @Test
    fun ioBuilder_sourcePathWithoutSourceSpecification_shouldThrowException() {

        // Arrange
        val builder = IOBuilder(this.kormConfiguration).root().kormRoot().migration().name("MyMigration")

        // Act
        val function = { builder.sourcePath() }

        // Assert
        function shouldThrow IllegalStateException::class
    }

    @Test
    fun ioBuilder_buildPath_shouldReturnPathOfCompiledFile() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").buildPath()

        // Assert
        result shouldEqual "path/to/my/project/build/main/at/michaelfoidl/test/migrations"
    }

    @Test
    fun ioBuilder_buildPathWithRoot_shouldReturnPathOfRootDirectory() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").buildPath(true)

        // Assert
        result shouldEqual "path/to/my/project/build/main"
    }

    @Test
    fun ioBuilder_buildPathExecutedTwice_shouldReturnSameResult() {

        // Arrange
        val builder = IOBuilder(this.kormConfiguration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration")

        // Act
        val result1 = builder.buildPath()
        val result2 = builder.buildPath()

        // Assert
        result1 shouldEqual result2
    }

    @Test
    fun ioBuilder_buildPathWithoutSourceSpecification_shouldThrowException() {

        // Arrange
        val builder = IOBuilder(this.kormConfiguration).root().kormRoot().migration().name("MyMigration")

        // Act
        val function = { builder.buildPath() }

        // Assert
        function shouldThrow IllegalStateException::class
    }

    @Test
    fun ioBuilder_packageName_shouldReturnPackageName() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration().name("MyMigration").packageName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.migrations"
    }

    @Test
    fun ioBuilder_qualifiedName_shouldReturnQualifiedName() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration().name("MyMigration").qualifiedName()

        // Assert
        result shouldEqual "at.michaelfoidl.test.migrations.MyMigration"
    }

    @Test
    fun ioBuilder_simpleName_shouldReturnSimpleName() {

        // Act
        val result = IOBuilder(this.kormConfiguration).root().kormRoot().migration().name("MyMigration").simpleName()

        // Assert
        result shouldEqual "MyMigration"
    }

    @Test
    fun ioBuilder_emptyPathParameters_shouldBeIgnored() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "",
                sourceDirectory = "src/main",
                buildDirectory = "build/classes/kotlin/main",
                rootDirectory = ""
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath()

        // Assert
        result shouldEqual "src/main/migrations"
    }

    @Test
    fun ioBuilder_emptyPackageParameters_shouldBeIgnored() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "",
                sourceDirectory = "src/main",
                buildDirectory = "build/classes/kotlin/main",
                rootDirectory = ""
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration().name("MyMigration").packageName()

        // Assert
        result shouldEqual "migrations"
    }

    @Test
    fun ioBuilder_absoluteWindowsRoot_shouldReturnAbsolutePath() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "at.michaelfoidl.test",
                sourceDirectory = "test/src/main",
                buildDirectory = "build/main",
                rootDirectory = "E:/path/to/my/project"
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath(true)

        // Assert
        result shouldEqual "E:/path/to/my/project/test/src/main"
    }

    @Test
    fun ioBuilder_absoluteLinuxRoot_shouldReturnAbsolutePath() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "at.michaelfoidl.test",
                sourceDirectory = "test/src/main",
                buildDirectory = "build/main",
                rootDirectory = "/path/to/my/project"
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath(true)

        // Assert
        result shouldEqual "/path/to/my/project/test/src/main"
    }

    @Test
    fun ioBuilder_windowsRootOnly_shouldReturnAbsolutePath() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "at.michaelfoidl.test",
                sourceDirectory = "test/src/main",
                buildDirectory = "build/main",
                rootDirectory = "E:"
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath(true)

        // Assert
        result shouldEqual "E:/test/src/main"
    }

    @Test
    fun ioBuilder_linuxRootOnly_shouldReturnAbsolutePath() {

        // Arrange
        val configuration: KormConfiguration = DefaultKormConfiguration(
                migrationPackage = "migrations",
                kormPackage = "at.michaelfoidl.test",
                sourceDirectory = "test/src/main",
                buildDirectory = "build/main",
                rootDirectory = "/"
        )

        // Act
        val result = IOBuilder(configuration).root().kormRoot().migration(IOBuilder.source, IOBuilder.build).name("MyMigration").sourcePath(true)

        // Assert
        result shouldEqual "/test/src/main"
    }
}
