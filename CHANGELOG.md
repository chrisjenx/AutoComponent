# Change Log


## 0.3.0-SNAPSHOT (2019/03/27)
- Updated to Kotlin 1.3.21
- Fixed @Inherited annotations, will now generate `inject(T)` for subclasses of `@Injection` annotated superclasses
- `AutoInjector` now only needs `rootComponent` and `injection` class, no need for `.class` in Java anymore