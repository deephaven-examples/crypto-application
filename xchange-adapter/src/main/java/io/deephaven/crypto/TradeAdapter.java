package io.deephaven.crypto;

public interface TradeAdapter {
  Trade adapt(org.knowm.xchange.dto.marketdata.Trade trade);
}
