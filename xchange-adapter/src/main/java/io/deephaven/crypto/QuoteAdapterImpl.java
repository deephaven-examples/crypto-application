package io.deephaven.crypto;

import java.util.Objects;
import org.knowm.xchange.dto.marketdata.Ticker;

public final class QuoteAdapterImpl implements QuoteAdapter {
  private final String exchangeName;

  public QuoteAdapterImpl(String exchangeName) {
    this.exchangeName = Objects.requireNonNull(exchangeName);
  }

  @Override
  public Quote adapt(Ticker ticker) {
    return Quote.newBuilder()
        .setExchange(exchangeName)
        .setTimestamp(ticker.getTimestamp() == null ? null : ticker.getTimestamp().toInstant())
        .setInstrument(ticker.getInstrument().toString())
        .setBid(DeephavenHelper.adapt(ticker.getBid()))
        .setAsk(DeephavenHelper.adapt(ticker.getAsk()))
        .setBidSize(DeephavenHelper.adapt(ticker.getBidSize()))
        .setAskSize(DeephavenHelper.adapt(ticker.getAskSize()))
        .build();
  }
}
