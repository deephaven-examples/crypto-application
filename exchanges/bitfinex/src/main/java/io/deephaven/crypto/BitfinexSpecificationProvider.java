package io.deephaven.crypto;

import info.bitrich.xchangestream.bitfinex.BitfinexStreamingExchange;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;

public class BitfinexSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "bitfinex";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(BitfinexStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USD)
            .addCurrencyPairs(CurrencyPair.ETH_USD)
            .build());
  }
}
