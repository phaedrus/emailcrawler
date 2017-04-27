package com.emailcrawling.rx;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkType;
import org.reactivestreams.Publisher;

import java.util.EnumSet;

public class EmailExtractor implements FlowableTransformer<String, String> {

  private LinkExtractor linkExtractor = LinkExtractor.builder()
          .linkTypes(EnumSet.of(LinkType.EMAIL))
          .build();

  @Override
  public Publisher<String> apply(@NonNull Flowable<String> upstream) {
    return upstream
            .flatMap(text -> Flowable.fromIterable(linkExtractor.extractLinks(text))
                    .map(linkSpan ->
                            text.substring(linkSpan.getBeginIndex(), linkSpan.getEndIndex()).trim()));
  }
}
