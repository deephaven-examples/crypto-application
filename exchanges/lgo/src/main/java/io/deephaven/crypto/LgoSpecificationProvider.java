package io.deephaven.crypto;

import info.bitrich.xchangestream.lgo.LgoStreamingExchange;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;

public class LgoSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "lgo";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(LgoStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USD)
            .addCurrencyPairs(CurrencyPair.ETH_USD)
            .addCurrencyPairs(CurrencyPair.DOGE_USD)
            .build());
  }
}
