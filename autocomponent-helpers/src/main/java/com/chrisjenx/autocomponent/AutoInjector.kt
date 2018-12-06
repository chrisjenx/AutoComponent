@file:JvmName("AutoInjector")
package com.chrisjenx.autocomponent

import java.lang.reflect.Method

var logger: (msg: String) -> Unit = { println("AutoComponentHelper: $it") }

private var autoComponent: Any? = null
private val injectMap: MutableMap<Class<*>, Method> = mutableMapOf()

/**
 * Try and find the AutoComponent from the component passed in, we cache it once found to reduce multiple hits
 */
internal fun findAutoComponent(rootComponent: Any): Any? {
    return autoComponent ?: rootComponent::class.java.methods
        .firstOrNull { it.returnType.simpleName == "AutoComponent" } // find method that returns AutoComponent
        ?.invoke(rootComponent) // get the AutoComponent
        ?.also { autoComponent = it }
        .also { if (it == null) logger("Failed to find auto component") }
}

internal fun <T> findInject(autoComponent: Any, injectClazz: Class<T>): Method? {
    return injectMap[injectClazz] ?: autoComponent::class.java.methods
        .firstOrNull {
            val p = it.parameterTypes
            when {
                p.size == 1 -> p[0] == injectClazz
                else -> false
            }
        }
        ?.also {
            // Add our shortcut :)
            injectMap[injectClazz] = it
        }
        .also {
            if (it == null) logger("Failed to find [inject(${injectClazz.simpleName})], did you remember to add [@Injector] to the class?")
        }
}

/**
 * For this to work make sure you have defined the following parts:
 *
 * 1. Define [@Injection] on classes you want to inject methods created for.
 *
 * ```
 * @Injection
 * class MyClass {
 * }
 * ```
 *
 * 2. Define the AutoComponent in your root component
 *
 * ```
 * @dagger.Component
 * interface AppComponent {
 *  fun autoComponent() : AutoComponent
 * }
 *
 * ```
 *
 * 3. Call this inject method on you [@Injecton] annotated class:
 *
 * ```
 * @Injection
 * class MyClass {
 *  init {
 *   inject(getAppComponent(), this, this::class.java)
 *  }
 * }
 * ```
 *
 * @param rootComponent this is the root component you defined the [AutoComponent] in.
 * @param inject this is the class you are injecting into
 * @param injectClazz this is the class definition for [inject]
 */
fun <T> inject(rootComponent: Any, inject: T, injectClazz: Class<out T>) {
    findAutoComponent(rootComponent)
        ?.let { autoComponent -> findInject(autoComponent, injectClazz) }
        ?.let { injectMethod -> injectMethod.invoke(autoComponent, inject) }
}

/**
 * Convenience method for [inject]
 *
 * @param rootComponent this is the root component you defined [AutoComponent] in.
 * @param T this is what we are going to try and inject into
 * @see inject
 */
inline fun <reified T> T.inject(rootComponent: Any) {
    inject(rootComponent, this, T::class.java)
}
