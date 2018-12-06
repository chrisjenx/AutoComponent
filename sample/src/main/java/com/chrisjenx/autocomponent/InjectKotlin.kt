package com.chrisjenx.autocomponent

import dagger.Lazy
import javax.inject.Inject

@Injection
class InjectKotlin {

    @Inject lateinit var dude: String
    @Inject lateinit var java: Lazy<InjectJava>


    init {
        inject(dagger)
    }

}

