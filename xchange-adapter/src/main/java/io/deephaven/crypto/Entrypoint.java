package io.deephaven.crypto;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.exceptions.ExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Entrypoint {

  private static final Logger log = LoggerFactory.getLogger(Entrypoint.class);

  public static List<StreamingExchange> execute(
      Observer<? super Trade> tradesObserver, Observer<? super Quote> quotesObserver) {
    final List<StreamingExchange> exchanges = new ArrayList<>();
    for (Specification specification : Specification.load()) {
      log.info("Starting exchange from '{}'", specification.streamingClass());
      final StreamingExchange exchange;
      try {
        exchange = StreamingExchangeFactory.INSTANCE.createExchange(specification.streamingClass());
      } catch (ExchangeException e) {
        log.error("Error, continuing...", e);
        continue;
      }
      connect(specification, exchange);
      subscribeTrades(specification, exchange, tradesObserver);
      subscribeQuotes(specification, exchange, quotesObserver);
      exchanges.add(exchange);
    }
    return exchanges;
  }

  private static void subscribeQuotes(
      Specification specification,
      StreamingExchange exchange,
      Observer<? super Quote> quotesObserver) {
    final QuoteAdapter quoteAdapter = specification.quoteAdapter();
    for (CurrencyPair currencyPair : specification.currencyPairs()) {
      exchange
          .getStreamingMarketDataService()
          .getTicker(currencyPair)
          .map(quoteAdapter::adapt)
          .subscribe(quotesObserver);
    }
  }

  private static void subscribeTrades(
      Specification specification,
      StreamingExchange exchange,
      Observer<? super Trade> tradesObserver) {
    final TradeAdapter tradeAdapter = specification.tradeAdapter();
    for (CurrencyPair currencyPair : specification.currencyPairs()) {
      exchange
          .getStreamingMarketDataService()
          .getTrades(currencyPair)
          .map(tradeAdapter::adapt)
          .subscribe(tradesObserver);
    }
  }

  private static void connect(Specification specification, StreamingExchange exchange) {
    final ProductSubscriptionBuilder subscription = ProductSubscription.create();
    for (CurrencyPair currencyPair : specification.currencyPairs()) {
      subscription.addTrades(currencyPair);
      subscription.addTicker(currencyPair);
    }
    if (!exchange.connect(subscription.build()).blockingAwait(10, TimeUnit.SECONDS)) {
      throw new RuntimeException("Unable to connect in 10 seconds");
    }
  }
}
