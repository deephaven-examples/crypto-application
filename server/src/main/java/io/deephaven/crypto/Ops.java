package io.deephaven.crypto;

import io.deephaven.api.ColumnName;
import io.deephaven.api.TableOperations;
import io.deephaven.api.agg.Count;
import io.deephaven.api.agg.Pair;
import io.deephaven.api.agg.spec.AggSpec;
import io.deephaven.api.filter.Filter;
import io.deephaven.qst.TableCreator;
import io.deephaven.qst.column.header.ColumnHeader;
import java.util.Arrays;
import java.util.Collections;

/** The operations for the crypto application. */
public class Ops {

  private static final ColumnName ID = ColumnName.of("Id");
  private static final ColumnName EXCHANGE = ColumnName.of("Exchange");
  private static final ColumnName INSTRUMENT = ColumnName.of("Instrument");
  private static final ColumnName TYPE = ColumnName.of("Type");
  private static final ColumnName TIMESTAMP = ColumnName.of("Timestamp");
  private static final ColumnName BID = ColumnName.of("Bid");
  private static final ColumnName ASK = ColumnName.of("Ask");
  private static final ColumnName BID_SIZE = ColumnName.of("BidSize");
  private static final ColumnName ASK_SIZE = ColumnName.of("AskSize");
  private static final ColumnName COUNT = ColumnName.of("Count");
  private static final ColumnName SIZE = ColumnName.of("Size");
  private static final ColumnName PRICE = ColumnName.of("Price");

  private static final ColumnName VOLUME = ColumnName.of("Volume");
  private static final ColumnName AVG_PRICE = ColumnName.of("AvgPrice");
  private static final ColumnName BASE_VOLUME = ColumnName.of("BaseVolume");
  private static final ColumnName CURRENCY = ColumnName.of("Currency");

  private static final ColumnName MID = ColumnName.of("Mid");
  private static final ColumnName USD = ColumnName.of("USD");

  public static <T extends TableOperations<T, ?>> T getTradesLatest(T trades) {
    return trades.lastBy(EXCHANGE, INSTRUMENT, TYPE);
  }

  public static <T extends TableOperations<T, T>> T getTradesSummary(T trades, T usdPrices) {
    return trades
        .updateView("BaseVolume=Price*Size")
        .aggBy(
            Arrays.asList(
                Count.of(COUNT),
                AggSpec.sum().aggregation(Pair.of(SIZE, VOLUME)),
                AggSpec.sum().aggregation(BASE_VOLUME),
                AggSpec.avg().aggregation(Pair.of(PRICE, AVG_PRICE))),
            Arrays.asList(EXCHANGE, INSTRUMENT, TYPE))
        .updateView("Currency=Instrument.split(`/`)[1]")
        .naturalJoin(usdPrices, Collections.singleton(CURRENCY), Collections.emptyList())
        .updateView("DollarVolume=USD * BaseVolume");
  }

  public static <T extends TableOperations<T, ?>> T getQuotesLatest(T quotes) {
    return quotes.lastBy(EXCHANGE, INSTRUMENT);
  }

  public static <T extends TableOperations<T, ?>> T getUsdPrices(
      TableCreator<T> c, T quotesLatest) {
    // Assume USDT is 1 to 1
    final T usdt =
        c.newTable(
            ColumnHeader.ofString("Currency")
                .header(ColumnHeader.ofDouble("USD"))
                .row("USD", 1.0)
                .row("USDT", 1.0)
                .newTable());
    return c.merge(
        usdt,
        quotesLatest
            .where(
                Arrays.asList(
                    Filter.isNotNull(TIMESTAMP), Filter.isNotNull(BID), Filter.isNotNull(ASK)))
            .where("Instrument.endsWith(`/USD`) || Instrument.endsWith(`/USDT`)")
            .view("Currency=Instrument.split(`/`)[0]", "Mid=(Bid+Ask)/2")
            .aggBy(
                AggSpec.median().aggregation(Pair.of(MID, USD)),
                Collections.singletonList(CURRENCY)));
  }
}
