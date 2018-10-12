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
import kotlin.reflect.KClass

internal object IOOracle {
    fun getMigrationFolderPath(configuration: KormConfiguration): String {
        return configuration.rootDirectory + "/" + PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.migrationPackage)
    }

    fun getTableFolderPath(configuration: KormConfiguration): String {
        return configuration.rootDirectory + "/" + PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.rootPackage) + "/tables"
    }

    fun getDatabaseFolderPath(configuration: KormConfiguration): String {
        return configuration.rootDirectory + "/" + PackageNameFilePathConverter.convertPackageNameToFilePath(configuration.databasePackage)
    }

    fun getMigrationFileName(databaseName: String, migrationPackage: String, databaseVersion: Long): String {
        return "$migrationPackage.${databaseName}_Migration_v${databaseVersion}_${databaseVersion + 1}"
    }

    fun getTableName(entityClass: KClass<*>): String {
        return entityClass.simpleName + "Table"
    }

    fun getDatabaseFileName(databaseName: String, databaseVersion: Long): String {
        return "${databaseName}_$databaseVersion"
    }

    fun getTablePackage(rootPackage: String): String {
//        return configuration.rootPackage + ".tables"
        return "test"
    }
}