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

package at.michaelfoidl.korm.core.exposed

object TransactionLevel {
    const val TRANSACTION_NONE: Int = 0
    const val TRANSACTION_READ_UNCOMMITTED: Int = 1
    const val TRANSACTION_READ_COMMITTED: Int = 2
    const val TRANSACTION_REPEATABLE_READ: Int = 4
    const val TRANSACTION_SERIALIZABLE: Int = 8
}