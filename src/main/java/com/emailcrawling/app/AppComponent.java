package com.emailcrawling.app;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

  void inject(App app);
}
