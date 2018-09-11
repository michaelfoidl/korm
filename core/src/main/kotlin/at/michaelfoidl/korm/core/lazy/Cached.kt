package at.michaelfoidl.korm.core.lazy

open class Cached<T>(initializer: (parameter: Any?) -> T) : Lazy<T, Any?>(initializer) {

    override val value: T
        get() {
            ensureThatIsInitialized(null)
            return this.initializedElement!!
        }

    override fun isInitialized(): Boolean {
        return initializedElement != null
    }
}