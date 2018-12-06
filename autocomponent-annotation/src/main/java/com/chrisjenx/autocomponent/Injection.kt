package com.chrisjenx.autocomponent

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * AutoComponent annotation to mark this class that we should generate a `AutoComponent#inject(T)` method for this class
 */
@Target(CLASS)
@Retention(BINARY)
annotation class Injection