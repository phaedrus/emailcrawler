package com.emailcrawling.crawler;

import com.emailcrawling.rx.PagePublisher;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.TextParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestCrawler {

  private static final String TEST_INPUT_DOMAIN = "google.com";

  @Mock
  private PagePublisher pagePublisher;
  @Mock
  private Page page;
  @Mock
  private WebURL webURL;
  @Mock
  private HtmlParseData parseData;
  @Mock
  private TextParseData textParseData;

  private Crawler crawler;

  @Before
  public void before() {
    crawler = new Crawler(TEST_INPUT_DOMAIN, pagePublisher);
  }

  @After
  public void after() {

  }

  @Test
  public void testShouldVisitInputDomain() {
    when(webURL.getURL()).thenReturn("http://google.com/interestingstuff");
    when(webURL.getDomain()).thenReturn("google.com");

    assertThat(crawler.shouldVisit(page, webURL)).isTrue();
  }


  @Test
  public void testShouldNotVisitOtherDomains() {
    when(webURL.getURL()).thenReturn("https://www.yahoo.com/search");
    when(webURL.getDomain()).thenReturn("yahoo.com");

    assertThat(crawler.shouldVisit(page, webURL)).isFalse();
  }

  @Test
  public void testShouldNotVisitInFilteredExtensionsList() {
    when(webURL.getURL()).thenReturn("http://www.google.com/search.css");

    assertThat(crawler.shouldVisit(page, webURL)).isFalse();
  }

  @Test
  public void testShouldPublishTextFromVisitedHtmlPages() {
    when(page.getParseData()).thenReturn(parseData);
    when(parseData.getText()).thenReturn("foo");

    crawler.visit(page);

    verify(pagePublisher).nextPage(anyString());
  }

  @Test
  public void testShouldNotPublishTextFromOtherTypesOfPages() {
    when(page.getParseData()).thenReturn(textParseData);

    crawler.visit(page);

    verifyNoMoreInteractions(pagePublisher);
  }
}
