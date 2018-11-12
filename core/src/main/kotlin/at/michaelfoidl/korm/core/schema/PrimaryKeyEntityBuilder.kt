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

package at.michaelfoidl.korm.core.schema

import at.michaelfoidl.korm.annotations.AutoIncrement
import at.michaelfoidl.korm.annotations.ColumnName
import at.michaelfoidl.korm.annotations.Indexed
import kotlin.reflect.KClass

internal class PrimaryKeyEntityBuilder internal constructor(
        propertyName: String,
        propertyReturnType: KClass<*>,
        isPropertyNullable: Boolean,
        columnNameAnnotation: ColumnName?,
        autoIncrementAnnotation: AutoIncrement?,
        foreignKeyAnnotation: at.michaelfoidl.korm.annotations.ForeignKey?,
        primaryKeyAnnotation: at.michaelfoidl.korm.annotations.PrimaryKey?,
        indexedAnnotation: Indexed?
) : ColumnEntityBuilder (
        propertyName,
        propertyReturnType,
        isPropertyNullable,
        columnNameAnnotation,
        autoIncrementAnnotation,
        foreignKeyAnnotation,
        primaryKeyAnnotation,
        indexedAnnotation
), PrimaryKeyBuilder {

    override fun toPrimaryKey(): PrimaryKey {
        return PrimaryKey(
                getName(),
                isAutoIncrement())
    }
}