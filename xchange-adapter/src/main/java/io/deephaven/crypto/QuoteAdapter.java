package io.deephaven.crypto;

import org.knowm.xchange.dto.marketdata.Ticker;

public interface QuoteAdapter {
  Quote adapt(Ticker ticker);
}
