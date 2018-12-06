package com.chrisjenx.autocomponent;

import static com.chrisjenx.autocomponent.ApplicationKt.getDagger;

import javax.inject.Inject;

@Injection
public class InjectJava {

  @Inject String dudeString;

  @Inject
  public InjectJava() {
    AutoInjector.inject(getDagger(), this, this.getClass());
  }

}
