package io.deephaven.crypto;

import info.bitrich.xchangestream.coinbasepro.CoinbaseProStreamingExchange;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.Ticker;

public class CoinbaseProSpecificationProvider
    implements SpecificationProvider, QuoteAdapter, TradeAdapter {

  private static final String NAME = "coinbase-pro";

  @Override
  public List<Specification> specifications() {
    return Collections.singletonList(
        ImmutableSpecification.builder()
            .streamingClass(CoinbaseProStreamingExchange.class)
            .quoteAdapter(this)
            .tradeAdapter(this)
            .addCurrencyPairs(CurrencyPair.BTC_USD)
            .addCurrencyPairs(CurrencyPair.ETH_USD)
            .addCurrencyPairs(CurrencyPair.DOGE_USD)
            .build());
  }

  @Override
  public Quote adapt(Ticker ticker) {

    final BigDecimal bidSize = ticker.getBidSize();
    final BigDecimal askSize = ticker.getAskSize();

    final double bidSizeDh = bidSize == null ? -Double.MAX_VALUE : bidSize.doubleValue();
    final double askSizeDh = askSize == null ? -Double.MAX_VALUE : askSize.doubleValue();

    return Quote.newBuilder()
        .setExchange(NAME)
        .setTimestamp(ticker.getTimestamp().toInstant())
        .setInstrument(ticker.getInstrument().toString())
        .setBid(ticker.getBid().doubleValue())
        .setAsk(ticker.getAsk().doubleValue())
        .setBidSize(bidSizeDh)
        .setAskSize(askSizeDh)
        .build();
  }

  @Override
  public Trade adapt(org.knowm.xchange.dto.marketdata.Trade trade) {
    return Trade.newBuilder()
        .setExchange(NAME)
        .setId(trade.getId())
        .setTimestamp(trade.getTimestamp().toInstant())
        .setInstrument(trade.getInstrument().toString())
        .setType(adapt(trade.getType()))
        .setPrice(trade.getPrice().doubleValue())
        .setSize(trade.getOriginalAmount().doubleValue())
        .build();
  }

  private static TradeType adapt(OrderType orderType) {
    switch (orderType) {
      case BID:
        return TradeType.BID;
      case ASK:
        return TradeType.ASK;
      case EXIT_ASK:
        return TradeType.EXIT_ASK;
      case EXIT_BID:
        return TradeType.EXIT_BID;
      default:
        throw new IllegalStateException("Unexpected order type " + orderType);
    }
  }
}
