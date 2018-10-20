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

package at.michaelfoidl.korm.testUtils

import at.michaelfoidl.korm.core.io.PackageNameFilePathConverter
import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.interfaces.Migration
import java.io.File
import java.nio.file.Paths

object BuildProcessFaker {
    fun compileMigration(fileName: String, sourceFolderPath: String, migrationPackage: String, buildFolderPath: String): Boolean {
        val sourceFilePath: String = listOf(
                Paths.get("").toAbsolutePath().toString(),
                sourceFolderPath,
                PackageNameFilePathConverter.convertPackageNameToFilePath(migrationPackage),
                "$fileName.kt"
        ).joinToString("/")
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    fun compileDatabase(fileName: String, sourceFolderPath: String, databasePackage: String, buildFolderPath: String): Boolean {
        val sourceFilePath: String = listOf(
                Paths.get("").toAbsolutePath().toString(),
                sourceFolderPath,
                PackageNameFilePathConverter.convertPackageNameToFilePath(databasePackage),
                "$fileName.kt"
        ).joinToString("/")
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    fun compileConfiguration(fileName: String, sourceFolderPath: String, buildFolderPath: String): Boolean {
        val sourceFilePath: String = listOf(
                Paths.get("").toAbsolutePath().toString(),
                sourceFolderPath,
                "$fileName.kt"
        ).joinToString("/")
        return Compiler.execute(File(sourceFilePath), File(buildFolderPath))
    }

    fun compileAndLoadMigration(fileName: String, sourceFolderPath: String, migrationPackage: String, buildFolderPath: String): Migration? {
        compileMigration(fileName, sourceFolderPath, migrationPackage, buildFolderPath)
        return ClassLoader(File(buildFolderPath), true).createInstance<Migration>("$migrationPackage.$fileName")
    }

    inline fun <reified T> compileAndLoadDatabase(fileName: String, sourceFolderPath: String, databasePackage: String, buildFolderPath: String): T? {
        compileDatabase(fileName, sourceFolderPath, databasePackage, buildFolderPath)
        return ClassLoader(File(buildFolderPath), true).createInstance<T>("$databasePackage.$fileName")
    }

    fun compileAndLoadConfiguration(fileName: String, sourceFolderPath: String, buildFolderPath: String): KormConfiguration? {
        compileConfiguration(fileName, sourceFolderPath, buildFolderPath)
        return ClassLoader(File(buildFolderPath), true).createInstance<KormConfiguration>(fileName)
    }
}