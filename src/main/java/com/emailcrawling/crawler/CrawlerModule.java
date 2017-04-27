package com.emailcrawling.crawler;

import dagger.Module;
import dagger.Provides;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Module
public class CrawlerModule {

  @Provides
  static CrawlController controller(
          CrawlConfig config,
          PageFetcher pageFetcher,
          RobotstxtServer robotstxtServer) {
    try {
      return new CrawlController(config, pageFetcher, robotstxtServer);
    } catch (Exception e) {
      throw new RuntimeException("Unable to create controller!", e);
    }
  }

  @Provides
  static RobotstxtServer robotstxtServer(PageFetcher pageFetcher) {
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    return new RobotstxtServer(robotstxtConfig, pageFetcher);
  }

  @Provides
  static PageFetcher pageFetcher(CrawlConfig config) {
    return new PageFetcher(config);
  }

  @Provides
  static CrawlConfig crawlConfig() {
    String crawlStorageFolder = "/tmp/.emailcrawl/";
    CrawlConfig config = new CrawlConfig();
    //config.setPolitenessDelay(100);
    config.setConnectionTimeout(3000);
    config.setUserAgentString("Email crawler");
    config.setCrawlStorageFolder(crawlStorageFolder);
    return config;
  }
}
