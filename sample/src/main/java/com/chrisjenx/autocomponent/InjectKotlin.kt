package com.chrisjenx.autocomponent

import dagger.Lazy
import javax.inject.Inject

@Injection
open class InjectKotlin {

    @Inject lateinit var dude: String
    @Inject lateinit var java: Lazy<InjectJava>

    init {
        inject(dagger)
    }
}

class SubClassInjectKotlin
@Inject constructor() : InjectKotlin() {

    @Inject lateinit var dude2: String

}

