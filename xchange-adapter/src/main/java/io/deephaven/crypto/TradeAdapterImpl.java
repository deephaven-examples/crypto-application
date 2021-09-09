package io.deephaven.crypto;

import java.util.Objects;
import org.knowm.xchange.dto.Order.OrderType;

public final class TradeAdapterImpl implements TradeAdapter {

  private final String exchangeName;

  public TradeAdapterImpl(String exchangeName) {
    this.exchangeName = Objects.requireNonNull(exchangeName);
  }

  @Override
  public Trade adapt(org.knowm.xchange.dto.marketdata.Trade trade) {
    return Trade.newBuilder()
        .setExchange(exchangeName)
        .setId(trade.getId())
        .setTimestamp(trade.getTimestamp() == null ? null : trade.getTimestamp().toInstant())
        .setInstrument(trade.getInstrument().toString())
        .setType(adapt(trade.getType()))
        .setPrice(DeephavenHelper.adapt(trade.getPrice()))
        .setSize(DeephavenHelper.adapt(trade.getOriginalAmount()))
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
