package com.chrisjenx.autocomponent;

import dagger.Lazy;
import javax.inject.Inject;

public class SubclassInjectJava extends InjectJava {

  @Inject Lazy<SubClassInjectKotlin> subClassInjectKotlinLazy;

  public SubclassInjectJava() {
    AutoInjector.inject(this, ApplicationKt.getDagger());
  }
}
