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

package at.michaelfoidl.korm.core.io.builder

import at.michaelfoidl.korm.interfaces.KormConfiguration

internal class IOBuilder(
        private val configuration: KormConfiguration
) {
    private var sourceDefinition: FolderDefintion? = null
    private var buildDefinition: FolderDefintion? = null
    private var steps: ArrayList<IOStep> = ArrayList()
    private val pathFilterFunction: (Boolean, IOStep) -> (Boolean) = { rootOnly, step ->
        ((rootOnly && step.importance == 1) || (!rootOnly && step.importance < 4)) && step.step.isNotBlank()
    }
    private var isAbsolute = false

    fun root(): IOBuilder {
        val newSteps = createStepsForPath(this.configuration.rootDirectory, 1)
        this.steps.addAll(newSteps)
        this.isAbsolute = newSteps.count() > 0 && isAbsolute(this.configuration.rootDirectory)
        return this
    }

    fun kormRoot(): IOBuilder {
        val newSteps = createStepsForPackage(this.configuration.kormPackage, 2)
        this.steps.addAll(newSteps)
        return this
    }

    fun migration(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = createStepsForPackage(this.configuration.migrationPackage, 3)
        this.steps.addAll(newSteps)

        return this
    }

    fun database(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = createStepsForPackage(this.configuration.databasePackage, 3)
        this.steps.addAll(newSteps)

        return this
    }

    fun table(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = createStepsForPackage(this.configuration.tablePackage, 3)
        this.steps.addAll(newSteps)

        return this
    }

    fun kormConfiguration(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        return this
    }

    fun databaseSchema(
            definition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = definition
        this.buildDefinition = definition

        return this
    }

    fun name(name: String): IOBuilder {
        this.steps.add(IOStep(name, 4))
        return this
    }

    fun sourcePath(rootOnly: Boolean = false): String {
        var result = this.steps
                .union(accessSourceDefinition().invoke(this.configuration))
                .asSequence()
                .filter { this.pathFilterFunction(rootOnly, it) }
                .sortedWith(compareBy { it.importance })
                .joinToString("/") { it.step }

        if (this.isAbsolute && !isAbsolute(result)) {
            result = "/$result"
        }

        return result
    }

    fun buildPath(rootOnly: Boolean = false): String {
        var result = this.steps
                .union(accessBuildDefinition().invoke(this.configuration))
                .asSequence()
                .filter { this.pathFilterFunction(rootOnly, it) }
                .sortedWith(compareBy { it.importance })
                .joinToString("/") { it.step }

        if (this.isAbsolute && !isAbsolute(result)) {
            result = "/$result"
        }

        return result
    }

    fun packageName(): String {
        return this.steps
                .asSequence()
                .filter { it.importance in 2..3 && it.step.isNotBlank() }
                .joinToString(".") { it.step }
    }

    fun simpleName(): String {
        return this.steps
                .asSequence()
                .filter { it.importance == 4 && it.step.isNotBlank() }
                .single()
                .step
    }

    fun qualifiedName(): String {
        return this.steps
                .asSequence()
                .filter { it.importance > 1 && it.step.isNotBlank() }
                .joinToString(".") { it.step }
    }

    private fun accessSourceDefinition(): FolderDefintion {
        try {
            return this.sourceDefinition!!
        } catch (exception: NullPointerException) {
            throw IllegalStateException("Specify source configuration.")
        }
    }

    private fun accessBuildDefinition(): FolderDefintion {
        try {
            return this.buildDefinition!!
        } catch (exception: NullPointerException) {
            throw IllegalStateException("Specify build configuration.")
        }
    }

    companion object {
        val source: FolderDefintion = { configuration ->
            createStepsForPath(configuration.sourceDirectory, 1)
        }

        val build: FolderDefintion = { configuration ->
            createStepsForPath(configuration.buildDirectory, 1)
        }

        val generatedSource: FolderDefintion = { configuration ->
            createStepsForPath(configuration.generatedSourceDirectory, 1)
        }

        val generatedBuild: FolderDefintion = { configuration ->
            createStepsForPath(configuration.generatedBuildDirectory, 1)
        }

        private fun createStepsForPath(path: String, importance: Int): Collection<IOStep> {
            return path
                    .split("/")
                    .map { IOStep(it, importance) }
        }

        private fun createStepsForPackage(packageName: String, importance: Int): Collection<IOStep> {
            return packageName
                    .split(".")
                    .map { IOStep(it, importance) }
        }

        private fun isAbsolute(path: String): Boolean {
            return path.startsWith("/") || path.matches("""^[A-Z]:.*""".toRegex())
        }
    }
}