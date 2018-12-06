## AutoComponent


Add `@Injecton` annotations to the classes you want the component to inject

```kotlin
@Injection
class MyClass {}

```


Expose the [AutoComponent] SubComponent via your root component.

```kotlin
@Singleton
@dagger.Component interface AppComponent {
  // The name doesn't matter, this will be satisfied once the annotation processor has run.
  fun autoComponent() : AutoComponent
}
```

Use the optional `AutoInjector.inject`s methods if you want.

Kotlin:

```kotlin

class MyClass {
  init {
    inject(getAppComponent())
  }
}

```

Java:

```java

public MyJavaClass {

  public MyJavaClass {
    AutoInjector.inject(getAppComponent(), this, this.getClass())
  }

}

```


### Manual Injection

I recommend you do manual injection, but you can mix and match where you feel it's "right".


```
class MyClass {

  init {
    // Manual injection is pretty trivial too
    getAppComponent().autoComponent().inject(this)
  }

}

```


### Dependencies (NOT YET DEPLOYED)

In your build.gradle

```

dependencies {

  // Optional - the helper classes, only need this if you want to use reflection.
  compileOnly "com.chrisjenx.autocomponent:autocomponent-helpers:0.1.0-SNAPSHOT"

  // Required - for kapt to work, can be compile, but we don't need them after kapt has run.
  compileOnly "com.chrisjenx.autocomponent:autocomponent-annotation:0.1.0-SNAPSHOT"

  // Required - generate the AutoComponent
  kapt "com.chrisjenx.autocomponent:autocomponent-processor:0.1.0-SNAPSHOT"

}


kapt {
  // Required, Dagger won't find the AutoComponent as Dagger needs correct types to compile other types generated
  // at run time.
  correctErrorTypes = true
}

```