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

@Injection(ignore = true)
class SkipClassInjectKotlin
@Inject constructor() : InjectKotlin() {
    @Inject lateinit var dude2: String
}

@Injection
class NewClassToInject {
    @Inject lateinit var dude2: String
    @Inject lateinit var dude3: String
}

@Injection
class NewClassToInject2 {
    @Inject lateinit var dude2: String
}


