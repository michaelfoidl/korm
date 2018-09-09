package at.michaelfoidl.korm.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class ForeignKey(val referencedClass: KClass<*>, val referencedProperty: String)