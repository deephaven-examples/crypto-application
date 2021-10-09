package io.deephaven.crypto.kafka;

import io.deephaven.crypto.Quote;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.Objects;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotesPublisher implements Observer<Quote>, Callback {

  private static final Logger log = LoggerFactory.getLogger(QuotesPublisher.class);

  private final KafkaProducer<String, Quote> producer;
  private Disposable disposable;

  public QuotesPublisher(KafkaProducer<String, Quote> producer) {
    this.producer = Objects.requireNonNull(producer);
  }

  @Override
  public void onSubscribe(Disposable d) {
    disposable = d;
  }

  @Override
  public void onNext(Quote quote) {
    final ProducerRecord<String, Quote> record =
        new ProducerRecord<>(Configuration.quotesTopic(), key(quote), quote);
    log.trace("{}", quote);
    producer.send(record, this);
  }

  private String key(Quote quote) {
    // Not using any keys
    return null;
  }

  @Override
  public void onError(Throwable e) {
    e.printStackTrace();
  }

  @Override
  public void onComplete() {}

  @Override
  public void onCompletion(RecordMetadata metadata, Exception exception) {
    if (exception != null) {
      disposable.dispose();
    }
  }
}
