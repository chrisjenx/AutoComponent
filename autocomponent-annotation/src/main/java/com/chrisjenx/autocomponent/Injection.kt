package com.chrisjenx.autocomponent

import java.lang.annotation.Inherited
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS

/**
 * AutoComponent annotation to mark this class that we should generate a `AutoComponent#inject(T)` method for this class
 */
@Inherited
@Target(CLASS)
@Retention(BINARY)
annotation class Injection