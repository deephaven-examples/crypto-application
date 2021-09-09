package io.deephaven.crypto;

import info.bitrich.xchangestream.binance.BinanceStreamingExchange;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;

public class BinanceSpecificationProvider implements SpecificationProvider {

  private static final String EXCHANGE_NAME = "binance";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .exchangeName(EXCHANGE_NAME)
            .streamingClass(BinanceStreamingExchange.class)
            .addCurrencyPairs(CurrencyPair.BTC_USDT)
            .addCurrencyPairs(CurrencyPair.ETH_USDT)
            .addCurrencyPairs(CurrencyPair.ETH_BTC)
            .build());
  }
}
