package io.deephaven.crypto;

import info.bitrich.xchangestream.poloniex2.PoloniexStreamingExchange;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;

public class PoloniexSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "poloniex";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(PoloniexStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USDT)
            .addCurrencyPairs(CurrencyPair.ETH_USDT)
            .build());
  }
}
