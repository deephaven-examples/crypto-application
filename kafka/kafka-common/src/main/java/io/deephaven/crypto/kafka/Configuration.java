package io.deephaven.crypto.kafka;

import io.deephaven.crypto.Quote;
import io.deephaven.crypto.Trade;
import org.apache.avro.Schema;

public class Configuration {
  public static String bootstrapServers() {
    return System.getProperty(
        "deephaven.bootstrap.servers",
        System.getenv().getOrDefault("DEEPHAVEN_BOOTSTRAP_SERVERS", "localhost:9092"));
  }

  public static String schemaRegistryUrl() {
    return System.getProperty(
        "deephaven.schema.registry.url",
        System.getenv()
            .getOrDefault("DEEPHAVEN_SCHEMA_REGISTRY_URL", "http://localhost:8081/api/ccompat"));
  }

  public static String quotesTopic() {
    return QuotesTopic.class.getName();
  }

  public static String tradesTopic() {
    return TradesTopic.class.getName();
  }

  public static String quotesTopicSchemaName() {
    return String.format("%s-%s", quotesTopic(), Quote.class.getName());
  }

  public static String tradesTopicSchemaName() {
    return String.format("%s-%s", tradesTopic(), Trade.class.getName());
  }

  public static Schema quoteSchema() {
    return Quote.getClassSchema();
  }

  public static Schema tradeSchema() {
    return Trade.getClassSchema();
  }
}
