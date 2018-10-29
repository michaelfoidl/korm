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

package at.michaelfoidl.korm.core.testUtils.tables

import kotlin.Long
import kotlin.String
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object SimpleEntity1WithAdditionalColumnTable : Table() {
    val thirdProperty: Column<String> = varchar("thirdProperty", 255)

    val id: Column<Long> = long("id")

    val name: Column<String> = varchar("name", 255)

    val otherName: Column<String> = varchar("otherName", 255)
}
