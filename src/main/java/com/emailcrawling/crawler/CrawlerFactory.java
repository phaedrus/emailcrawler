package com.emailcrawling.crawler;

import com.emailcrawling.rx.PagePublisher;
import edu.uci.ics.crawler4j.crawler.CrawlController;

import javax.inject.Inject;
import javax.inject.Named;

public class CrawlerFactory implements CrawlController.WebCrawlerFactory<Crawler> {

  private final String crawlDomain;
  private final PagePublisher pagePublisher;

  @Inject
  CrawlerFactory(@Named("crawl-domain") String crawlDomain, PagePublisher pagePublisher) {
    this.crawlDomain = crawlDomain;
    this.pagePublisher = pagePublisher;
  }

  @Override
  public Crawler newInstance() throws Exception {
    return new Crawler(crawlDomain, pagePublisher);
  }
}