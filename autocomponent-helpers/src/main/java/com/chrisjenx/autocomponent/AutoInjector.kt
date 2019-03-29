@file:JvmName("AutoInjector")

package com.chrisjenx.autocomponent

import java.lang.reflect.Method

var logger: (msg: String) -> Unit = { println("AutoComponentHelper: $it") }

private var autoComponent: Pair<Any, Any>? = null
private val injectMap: MutableMap<Class<*>, Method> = mutableMapOf()

/**
 * Try and find the AutoComponent from the component passed in, we cache it once found to reduce multiple hits
 */
internal fun findAutoComponent(rootComponent: Any): Any? {
    // See if we have found anything. and check if it's the same root!
    val foundAuto = autoComponent?.let { ra ->
        // Let's see if we still have the same root component (first item)
        if (ra.first == rootComponent) ra.second
        else null
    }
    return foundAuto ?: rootComponent::class.java.methods
        .firstOrNull { it.returnType.simpleName == "AutoComponent" } // find method that returns AutoComponent
        ?.invoke(rootComponent) // get the AutoComponent
        // Save the component if we found it
        ?.also { autoComponent = rootComponent to it }
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
        .let {
            if (it != null) return@let it
            val superClass = injectClazz.superclass
            val interfaces = injectClazz.interfaces
            return@let when {
                superClass != null && superClass != Any::class.java -> findInject(autoComponent, superClass)
                interfaces != null -> {
                    var findInterface: Method? = null
                    for (i in interfaces) {
                        findInterface = findInject(autoComponent, i)
                        if (findInterface != null) break
                    }
                    findInterface
                }
                else -> {
                    logger("Failed to find [inject(${injectClazz.simpleName})], did you remember to add [@Injector] to the class?")
                    null
                }
            }
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
 *   inject(getAppComponent(), this)
 *  }
 * }
 * ```
 *
 * @param rootComponent this is the root component you defined the [AutoComponent] in.
 * @param inject this is the class you are injecting into
 * @receiver the class you want to inject into
 */
//fun <T> inject(rootComponent: Any, inject: T, injectClazz: Class<out T>) {
fun <T : Any> T.inject(rootComponent: Any) {
    findAutoComponent(rootComponent)?.let { autoComponent ->
        findInject(autoComponent, this.javaClass)?.invoke(autoComponent, this)
    }
}
