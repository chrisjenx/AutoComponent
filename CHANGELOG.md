# Change Log

## 0.6.0-SNAPSHOT (2019/09/07)
- Updated dependencies
- Enabled incremental support

## 0.5.0-SNAPSHOT (2019/03/29)

### AutoInjectHelper
- Check if the rootComponent changes so we don't cache old components to re-fetch AutoComponent

## 0.4.0-SNAPSHOT (2019/03/28)
- Add ignore to `@Injection(ignore = true)`. 

## 0.3.0-SNAPSHOT (2019/03/27)
- Updated to Kotlin 1.3.21
- Fixed @Inherited annotations, will now generate `inject(T)` for subclasses of `@Injection` annotated superclasses
- `AutoInjector` now only needs `rootComponent` and `injection` class, no need for `.class` in Java anymore