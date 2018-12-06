package com.chrisjenx.autocomponent

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {
    fun autoComponent(): AutoComponent
    fun subComponent(): SubComponent.Builder
}

@Module(subcomponents = [SubComponent::class])
class MainModule {
    @Provides fun stringProvider(): String = "dude"
}

@Subcomponent
interface SubComponent {
    fun inject(inject: InjectKotlin)

    @Subcomponent.Builder
    interface Builder {
        fun create(): SubComponent
    }
}
