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

package at.michaelfoidl.korm.core.test.schema

import at.michaelfoidl.korm.annotations.AutoIncrement
import at.michaelfoidl.korm.annotations.ColumnName
import at.michaelfoidl.korm.annotations.Indexed
import at.michaelfoidl.korm.core.schema.PrimaryKey
import at.michaelfoidl.korm.core.schema.PrimaryKeyBuilder
import at.michaelfoidl.korm.core.testUtils.schema.entities.EntityWithAnnotatedProperties
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldNotBe
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaField

class PrimaryKeyBuilderTests {

    @Test
    fun primaryKeyBuilder_toPrimaryKey_shouldReturnCorrespondingPrimaryKey() {

        // Arrange
        val builder = PrimaryKeyBuilder(
                "primaryKeyColumn",
                Long::class,
                false,
                EntityWithAnnotatedProperties::primaryKeyColumn.javaField!!.getAnnotation(ColumnName::class.java),
                EntityWithAnnotatedProperties::primaryKeyColumn.javaField!!.getAnnotation(AutoIncrement::class.java),
                EntityWithAnnotatedProperties::primaryKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.ForeignKey::class.java),
                EntityWithAnnotatedProperties::primaryKeyColumn.javaField!!.getAnnotation(at.michaelfoidl.korm.annotations.PrimaryKey::class.java),
                EntityWithAnnotatedProperties::primaryKeyColumn.javaField!!.getAnnotation(Indexed::class.java))

        // Act
        val result = builder.toPrimaryKey()

        // Assert
        result shouldNotBe null
        result shouldBeInstanceOf PrimaryKey::class
    }
}