package com.emailcrawling.crawler;

import com.emailcrawling.rx.PagePublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CrawlerFactoryTest {

  private static final String TEST_INPUT_DOMAIN = "google.com";

  @Mock
  PagePublisher pagePublisher;

  private CrawlerFactory crawlerFactory;

  @Before
  public void setUp() throws Exception {

    crawlerFactory = new CrawlerFactory(TEST_INPUT_DOMAIN, pagePublisher);
  }

  @Test
  public void testThatDomainIsSet() throws Exception {
    Crawler crawler = crawlerFactory.newInstance();

    assertThat(crawler.getCrawlDomain()).isEqualTo(TEST_INPUT_DOMAIN);
  }

  @Test
  public void testThatPublisherIsSet() throws Exception {
    Crawler crawler = crawlerFactory.newInstance();

    assertThat(crawler.getPagePublisher()).isEqualTo(pagePublisher);
  }


}