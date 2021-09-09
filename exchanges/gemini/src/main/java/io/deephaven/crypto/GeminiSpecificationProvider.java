package io.deephaven.crypto;

import info.bitrich.xchangestream.gemini.GeminiStreamingExchange;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.Collections;
import java.util.List;

public class GeminiSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "gemini";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(GeminiStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USD)
            .addCurrencyPairs(CurrencyPair.ETH_USD)
            .addCurrencyPairs(CurrencyPair.DOGE_USD)
            .build());
  }
}
