## AutoComponent


Add `@Injection` annotations to the classes you want the component to inject

```kotlin
@Injection
class MyClass {}

```

You may annotate super classes with `@Injection` and all subclasses will be processed too.

In some cases you may want to ignore/skip a subclass as you don't do any injection or want to create a `@Subcomponent` 
etc. In those cases you can use the `ignore` property on the annotation (defaults to `false`), this will tell the 
annotation processor to not create an `inject(T)` for this subclass. 

Warning: any subclass of `@Injection(ignore=true)` will be ignored too. You will need to annotate subclasses with 
`@Injection()` to re-include those.  

```kotlin
@Injection(ignore = true)
class IgnoreSubclass : MyClass 
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
@Injection
class MyClass {
  init {
    inject(getAppComponent())
  }
}

```

Java:

```java
@Injection
public class MyJavaClass {

  public MyJavaClass() {
    AutoInjector.inject(this, getAppComponent());
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

### Incremental Compilation

There is a bug currently where during incremental compilation that AutComponent will regenerate the `AutoComponent`
but it doesn't pass AutoComponent to Dagger for it to run against that.

Basically if you get `AutoComponentImpl is not abstract and does not override abstract method ...` two things, make a
change in the MainComponent you added AutoComponent too (this triggers Dagger to recompile), or clean build.


### Dependencies (NOT YET DEPLOYED)

In your build.gradle

```

dependencies {

  // Optional - the helper classes, only need this if you want to use reflection.
  compileOnly "com.chrisjenx.autocomponent:autocomponent-helpers:0.6.0-SNAPSHOT"

  // Required - for kapt to work, can be compile, but we don't need them after kapt has run.
  compileOnly "com.chrisjenx.autocomponent:autocomponent-annotation:0.6.0-SNAPSHOT"

  // Required - generate the AutoComponent
  kapt "com.chrisjenx.autocomponent:autocomponent-processor:0.6.0-SNAPSHOT"

}


kapt {
  // Required, Dagger won't find the AutoComponent as Dagger needs correct types to compile other types generated
  // at run time.
  correctErrorTypes = true
}

```