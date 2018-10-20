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

    fun root(): IOBuilder {
        val newSteps = this.configuration.rootDirectory
                .split("/")
                .map { IOStep(it, 1) }
        this.steps.addAll(newSteps)
        return this
    }

    fun kormRoot(): IOBuilder {
        val newSteps = this.configuration.kormPackage
                .split(".")
                .map { IOStep(it, 2) }
        this.steps.addAll(newSteps)
        return this
    }

    fun migration(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = this.configuration.migrationPackage
                .split(".")
                .map { IOStep(it, 3) }
        this.steps.addAll(newSteps)

        return this
    }

    fun database(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = this.configuration.databasePackage
                .split(".")
                .map { IOStep(it, 3) }
        this.steps.addAll(newSteps)

        return this
    }

    fun table(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ): IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        val newSteps = this.configuration.tablePackage
                .split(".")
                .map { IOStep(it, 3) }
        this.steps.addAll(newSteps)

        return this
    }

    fun configuration(
            sourceDefinition: FolderDefintion? = null,
            buildDefinition: FolderDefintion? = null
    ) : IOBuilder {
        this.sourceDefinition = sourceDefinition
        this.buildDefinition = buildDefinition

        return this
    }

    fun name(name: String): IOBuilder {
        this.steps.add(IOStep(name, 4))
        return this
    }

    fun sourcePath(rootOnly: Boolean = false): String {
        val result = this.steps.union(accessSourceDefinition().invoke(this.configuration))
        return result
                .asSequence()
                .filter { this.pathFilterFunction(rootOnly, it) }
                .sortedWith(compareBy { it.importance })
                .joinToString("/") { it.step }
    }

    fun buildPath(rootOnly: Boolean = false): String {
        val result = this.steps.union(accessBuildDefinition().invoke(this.configuration))
        return result
                .asSequence()
                .filter { this.pathFilterFunction(rootOnly, it) }
                .sortedWith(compareBy { it.importance })
                .joinToString("/") { it.step }
    }

    fun packageName(): String {
        return this.steps
                .asSequence()
                .filter { it.importance in 2..3  && it.step.isNotBlank() }
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

    private fun prepareBuild(rootOnly: Boolean) {
        if (rootOnly) {
            this.steps = ArrayList(this.steps.filter { it.importance == 1 })
        }
        this.steps.sortWith(compareBy { it.importance })
    }


    companion object {
        val source: FolderDefintion = { configuration ->
            configuration.sourceDirectory
                    .split("/")
                    .map { IOStep(it, 1) }
        }

        val build: FolderDefintion = { configuration ->
            configuration.buildDirectory
                    .split("/")
                    .map { IOStep(it, 1) }
        }

        val generatedSource: FolderDefintion = { configuration ->
            configuration.generatedSourceDirectory
                    .split("/")
                    .map { IOStep(it, 1) }
        }

        val generatedBuild: FolderDefintion = { configuration ->
            configuration.generatedBuildDirectory
                    .split("/")
                    .map { IOStep(it, 1) }
        }
    }
}