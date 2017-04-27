package com.emailcrawling.app;

import com.emailcrawling.crawler.CrawlerModule;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

@Module(includes = CrawlerModule.class)
class AppModule {

  private final String crawlDomain;

  AppModule(String crawlDomain) {
    this.crawlDomain = crawlDomain;
  }

  @Named("crawl-domain")
  @Provides
  String crawlDomain() {
    return crawlDomain;
  }
}
