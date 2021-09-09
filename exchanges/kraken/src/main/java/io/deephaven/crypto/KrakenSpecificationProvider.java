package io.deephaven.crypto;

import info.bitrich.xchangestream.kraken.KrakenStreamingExchange;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;

public class KrakenSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "kraken";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(KrakenStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USD)
            .addCurrencyPairs(CurrencyPair.ETH_USD)
            .addCurrencyPairs(CurrencyPair.DOGE_USD)
            .build());
  }
}
