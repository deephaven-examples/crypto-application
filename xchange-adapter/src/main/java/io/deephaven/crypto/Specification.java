package io.deephaven.crypto;

import info.bitrich.xchangestream.core.StreamingExchange;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.immutables.value.Value.Immutable;
import org.knowm.xchange.currency.CurrencyPair;

@Immutable
public abstract class Specification {

  public static List<Specification> load() {
    return StreamSupport.stream(SpecificationProvider.load().spliterator(), false)
        .map(SpecificationProvider::specifications)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public abstract QuoteAdapter quoteAdapter();

  public abstract TradeAdapter tradeAdapter();

  public abstract Class<? extends StreamingExchange> streamingClass();

  public abstract List<CurrencyPair> currencyPairs();
}
