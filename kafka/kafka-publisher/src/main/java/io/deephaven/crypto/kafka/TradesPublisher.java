package io.deephaven.crypto.kafka;

import io.deephaven.crypto.Trade;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.Objects;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TradesPublisher implements Observer<Trade>, Callback {

  private static final Logger log = LoggerFactory.getLogger(TradesPublisher.class);

  private final KafkaProducer<String, Trade> producer;
  private Disposable disposable;

  public TradesPublisher(KafkaProducer<String, Trade> producer) {
    this.producer = Objects.requireNonNull(producer);
  }

  @Override
  public void onSubscribe(Disposable d) {
    disposable = d;
  }

  @Override
  public void onNext(Trade trade) {
    final ProducerRecord<String, Trade> record =
        new ProducerRecord<>(Configuration.tradesTopic(), key(trade), trade);
    log.trace("{}", trade);
    producer.send(record, this);
  }

  private String key(Trade trade) {
    // Not using any key
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
