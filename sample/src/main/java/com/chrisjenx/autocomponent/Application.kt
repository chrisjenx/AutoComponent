package com.chrisjenx.autocomponent

val dagger: MainComponent by lazy { DaggerMainComponent.builder().build() }

fun main(args: Array<String>) {
    if(InjectJava().dudeString != "dude") {
        throw IllegalArgumentException("Did not inject Java")
    }
    if(InjectKotlin().dude != "dude") {
        throw IllegalArgumentException("Did not inject Kotlin")
    }

    if(SubClassInjectKotlin().dude2 != "dude") {
        throw IllegalArgumentException("Did not inject SubClassKotlin")
    }

    if(SubclassInjectJava().subClassInjectKotlinLazy == null) {
        throw IllegalArgumentException("Did not injet SubclassInjectJava")
    }
}
