package com.emailcrawling.rx;

import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;


public class PagePublisherTest {

  private PagePublisher pagePublisher;

  @Before
  public void setUp() throws Exception {
    pagePublisher = new PagePublisher();
  }

  @Test
  public void testNextPageEmitsNewPage() throws Exception {
    TestSubscriber<String> testSubscriber = pagePublisher.pageStream().test();

    pagePublisher.nextPage("foo");

    testSubscriber.assertValue("foo");
  }

}