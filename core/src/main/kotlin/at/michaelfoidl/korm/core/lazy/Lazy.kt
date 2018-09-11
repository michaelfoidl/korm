package at.michaelfoidl.korm.core.lazy

import kotlin.Lazy


abstract class Lazy<ContentT, ParameterT: Any?>(
        private var initializer: (parameter: ParameterT) -> ContentT
): Lazy<ContentT> {
    protected var initializedElement: ContentT? = null

    override fun isInitialized(): Boolean {
        return initializedElement != null
    }

    fun invalidate() {
        this.initializedElement = null
    }

    protected fun ensureThatIsInitialized(parameter: ParameterT) {
        if (!isInitialized()) {
            this.initializedElement = initializer(parameter)
        }
    }
}