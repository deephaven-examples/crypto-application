package io.deephaven.crypto.kafka;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;
import io.deephaven.crypto.Entrypoint;
import io.deephaven.crypto.Quote;
import io.deephaven.crypto.Trade;
import java.time.Duration;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class KafkaPublisher {
  public static void main(String[] args) throws InterruptedException {

    final String deephavenDelay = System.getenv("DEEPHAVEN_DELAY");
    if (deephavenDelay != null) {
      System.out.printf("Sleeping for '%s' before starting...%n", deephavenDelay);
      Thread.sleep(Duration.parse(deephavenDelay).toMillis());
    }

    final Properties props = new Properties();

    final String bootstrapServers = Configuration.bootstrapServers();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    final String schemaRegistryUrl = Configuration.schemaRegistryUrl();
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

    props.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        org.apache.kafka.common.serialization.StringSerializer.class);

    props.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        io.confluent.kafka.serializers.KafkaAvroSerializer.class);

    props.put(
        AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicRecordNameStrategy.class);

    // props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, true);

    final KafkaProducer<String, Trade> tradesProducer = new KafkaProducer<>(props);
    final KafkaProducer<String, Quote> quotesProducer = new KafkaProducer<>(props);

    execute(tradesProducer, quotesProducer);
  }

  private static void execute(
      KafkaProducer<String, Trade> tradesProducer, KafkaProducer<String, Quote> quotesProducer) {
    Entrypoint.execute(new TradesPublisher(tradesProducer), new QuotesPublisher(quotesProducer));
  }
}
