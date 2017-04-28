package com.emailcrawling.crawler;

import com.emailcrawling.rx.PagePublisher;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Subclass of web crawler that only crawls pages on the given sub-domain and
 * publishes the contains of all crawled pages on the give PagePublisher.
 */
class Crawler extends WebCrawler {
  private static final String MEDIA_EXTENSION_REGEX =
          ".*(\\.(css|js|bmp|gif|jpe?g|png|ico|tiff?|mid|mp2|mp3|mp4|"
                  +"wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))";
  private static final Pattern MEDIA_FILE_PATTERN = Pattern.compile(MEDIA_EXTENSION_REGEX + "$");

  private final PagePublisher pagePublisher;
  private final String crawlDomain;
  private final String wwwCrawlDomain;
  private final String webCrawlDomain;

  @Inject
  Crawler(@Named("crawl-domain") String crawlDomain,
          PagePublisher pagePublisher) {
    this.pagePublisher = pagePublisher;
    this.crawlDomain = crawlDomain;
    this.wwwCrawlDomain = "www." + crawlDomain;
    this.webCrawlDomain = "web." + crawlDomain;
  }

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String lowerCaseUrl = url.getURL().toLowerCase();
    return !isMediaUrl(lowerCaseUrl) && matchesDomain(url);
  }

  private boolean isMediaUrl(final String url) {
    return MEDIA_FILE_PATTERN.matcher(url).matches();
  }

  private boolean matchesDomain(WebURL url) {
    String subDomain = (url.getSubDomain() + "." + url.getDomain()).toLowerCase();
    return subDomain.equals(crawlDomain) || subDomain.equals(webCrawlDomain) || subDomain.equals(wwwCrawlDomain);
  }

  @Override
  public void visit(Page page) {
    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      if (text != null) {
        pagePublisher.nextPage(text);
      }
    }
  }

  String getCrawlDomain() {
    return crawlDomain;
  }

  PagePublisher getPagePublisher() {
    return pagePublisher;
  }
}
