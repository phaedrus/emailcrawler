package com.emailcrawling.app;

import com.emailcrawling.crawler.CrawlerFactory;
import com.emailcrawling.rx.EmailExtractor;
import com.emailcrawling.rx.PagePublisher;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class App {

  private static final int NUMBER_OF_CRAWLERS = 7;

  private final String[] args;

  @Inject
  CrawlController controller;
  @Inject
  CrawlerFactory crawlerFactory;
  @Inject PagePublisher pagePublisher;

  private App(String[] args) {
    this.args = args;
  }

  private void run() {
    if (args.length < 1) {
      System.err.print("Invalid Usage. Please provide a domain.");
      return;
    }
    String domain = sanitizeDomain(args[0]);
    System.out.println(String.format("Crawling domain: %s", domain));
    setupComponent(domain);
    createCrawlFlowable(domain)
            .doOnError(System.err::println)
            .blockingForEach(System.out::println);
    System.out.println("Finished crawl");
    System.exit(0);
  }

  private String sanitizeDomain(String crawlDomain) {
    return crawlDomain.replace("www.", "");
  }

  private void setupComponent(String crawlDomain) {
    DaggerAppComponent.builder()
            .appModule(new AppModule(crawlDomain))
            .build()
            .inject(this);
  }

  private Flowable<String> createCrawlFlowable(final String domain) {
    return Flowable
            .create(e -> {
              startCrawl(domain);
              e.onNext(new Object());
              controller.waitUntilFinish();
              pagePublisher.complete();
              e.onComplete();
              controller.shutdown();

            }, BackpressureStrategy.MISSING)
            .flatMap(o -> createEmailFlowable(domain).subscribeOn(Schedulers.computation()))
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation());
  }

  private void startCrawl(String domain) {
    controller.addSeed("https://" + domain);
    controller.startNonBlocking(crawlerFactory, NUMBER_OF_CRAWLERS);
  }

  private Flowable<String> createEmailFlowable(final String emailDomain) {
    return pagePublisher
            .pageStream()
            .onBackpressureBuffer()
            .compose(new EmailExtractor())
            .filter(email -> email.endsWith(emailDomain))
            .distinct();
  }


  public static void main(String[] args) {
    new App(args).run();
  }

}
