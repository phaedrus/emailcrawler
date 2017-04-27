package com.emailcrawling.rx;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;

public class EmailExtractorTest {

  private EmailExtractor emailExtractor;

  @Before
  public void before() {
    emailExtractor = new EmailExtractor();
  }

  @Test
  public void testEmailsAreExtrated() {
    Flowable<String> textObservable = Flowable.just("foo noah.durell@gmail.com foo");

    TestSubscriber<String> testSubscriber = TestSubscriber.create();
    emailExtractor.apply(textObservable).subscribe(testSubscriber);

    testSubscriber.assertValue("noah.durell@gmail.com");
  }

  @Test
  public void testInternationalEmailExtracted() {
    Flowable<String> textObservable = Flowable.just("blah blah üñîçøðé@üñîçøðé.com balbh");

    TestSubscriber<String> testSubscriber = TestSubscriber.create();
    emailExtractor.apply(textObservable).subscribe(testSubscriber);

    testSubscriber.assertValue("üñîçøðé@üñîçøðé.com");
  }

  @Test
  public void testNoEmailsFound() {
    Flowable<String> textObservable = Flowable.just("blah blah @@@ bar sdf balbh");

    TestSubscriber<String> testSubscriber = TestSubscriber.create();
    emailExtractor.apply(textObservable).subscribe(testSubscriber);

    testSubscriber.assertValueCount(0);
  }

}