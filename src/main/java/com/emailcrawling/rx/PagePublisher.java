package com.emailcrawling.rx;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PagePublisher {

  private final PublishProcessor<String> pageRelay = PublishProcessor.create();

  @Inject
  public PagePublisher() {}

  public void nextPage(String page) {
    pageRelay.onNext(page);
  }

  public Flowable<String> pageStream() {
    return pageRelay.hide();
  }

  public void complete() {
    pageRelay.onComplete();
  }
}
