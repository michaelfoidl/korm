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

package at.michaelfoidl.korm.core.io

import at.michaelfoidl.korm.interfaces.KormConfiguration
import at.michaelfoidl.korm.types.TypeWrapper
import kotlin.reflect.KClass

internal object IOOracle {

    private const val TABLE_FOLDER_NAME: String = "tables"
    private const val DATABASE_FOLDER_NAME: String = "database"

    fun getRootFolderPath(configuration: KormConfiguration): String {
        return configuration.sourceDirectory
    }

    fun getMigrationFolderPath(configuration: KormConfiguration): String {
        return listOf(
                configuration.sourceDirectory,
                PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.rootPackage),
                PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.migrationPackage)
        ).joinToString("/")
    }

    fun getTableFolderPath(configuration: KormConfiguration): String {
        return listOf(
                configuration.sourceDirectory,
                PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.rootPackage),
                TABLE_FOLDER_NAME
        ).joinToString("/")
    }

    fun getDatabaseFolderPath(configuration: KormConfiguration): String {
        return listOf(
                configuration.sourceDirectory,
                PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.rootPackage),
                DATABASE_FOLDER_NAME
        ).joinToString("/")
    }

    fun getMigrationName(databaseName: String, databaseVersion: Long): String {
        return "${databaseName}_Migration_v${databaseVersion}_${databaseVersion + 1}"
    }

    fun getTableName(entityClass: KClass<*>): String {
        return entityClass.simpleName + "Table"
    }

    fun getTableName(element: TypeWrapper): String {
        return ElementConverter.getSimpleName(element.typeName!!) + "Table"
    }

    fun getDatabaseName(databaseName: String, databaseVersion: Long): String {
        return "${databaseName}_$databaseVersion"
    }

    fun getMigrationPackage(configuration: KormConfiguration): String {
        return "${configuration.rootPackage}.${configuration.migrationPackage}"
    }

    fun getTablePackage(configuration: KormConfiguration): String {
        return "${configuration.rootPackage}.$TABLE_FOLDER_NAME"
    }

    fun getDatabasePackage(configuration: KormConfiguration): String {
        return "${configuration.rootPackage}.$DATABASE_FOLDER_NAME"
    }
}