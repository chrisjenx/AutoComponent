package com.chrisjenx.autocomponent

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

class AutoInjectorKtTest {

    @Test
    fun `find auto component`() {
        val root = RootComponentImpl()

        val autoComponent = findAutoComponent(root)

        println("Found AutoComponent $autoComponent")

        assertThat(autoComponent, notNullValue())
    }

    @Test
    fun `find inject method`() {
        val method = findInject(AutoComponentImpl(), InjectThisClass::class.java)

        println("Found Inject Method $method")

        assertThat(method, notNullValue())
    }

    @Test
    fun `full injection`() {
        val injectInto = InjectThisClass()
        injectInto.inject(RootComponentImpl())

        assertThat(injectInto.setString, equalTo("We have been injected!"))
    }

    class InjectThisClass {
        var setString: String? = null
    }

    interface AutoComponent {
        fun inject(any: InjectThisClass)
    }

    class AutoComponentImpl : AutoComponent {
        override fun inject(any: InjectThisClass) {
            any.setString = "We have been injected!"
        }
    }

    interface RootComponent {
        fun autoComponent(): AutoComponent
    }

    class RootComponentImpl : RootComponent {
        val autoComponent: AutoComponent by lazy { AutoComponentImpl() }
        override fun autoComponent(): AutoComponent = autoComponent
    }
}
