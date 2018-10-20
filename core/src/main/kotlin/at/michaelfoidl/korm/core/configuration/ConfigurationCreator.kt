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

package at.michaelfoidl.korm.core.configuration

import at.michaelfoidl.korm.core.io.IOOracle
import at.michaelfoidl.korm.interfaces.KormConfiguration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File

class ConfigurationCreator {
    fun createKormConfiguration(configuration: KormConfiguration): String {
        val configurationBuilder = IOOracle.getKormConfigurationBuilder(configuration)
        val configurationName = configurationBuilder.simpleName()
        FileSpec.builder("", configurationName)
                .addType(
                        TypeSpec.classBuilder(configurationName)
                                .superclass(DefaultKormConfiguration::class.asTypeName())
                                .addSuperclassConstructorParameter("\"${configuration.migrationPackage}\"")
                                .addSuperclassConstructorParameter("\"${configuration.kormPackage}\"")
                                .addSuperclassConstructorParameter("\"${configuration.sourceDirectory}\"")
                                .addSuperclassConstructorParameter("\"${configuration.buildDirectory}\"")
                                .addSuperclassConstructorParameter("\"${configuration.rootDirectory.replace("\\", "\\\\")}\"")
                                .build()
                )
                .build()
                .writeTo(File(configurationBuilder.sourcePath(true), ""))
        return configurationName
    }
}