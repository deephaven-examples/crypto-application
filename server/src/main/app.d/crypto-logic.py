import jpy

from deephaven import kafka_consumer as kt
from deephaven.table import Table
from deephaven.stream.kafka.consumer import TableType, KeyValueSpec

def ops():
    return jpy.get_type('io.deephaven.crypto.Ops')

def configuration():
    return jpy.get_type('io.deephaven.crypto.kafka.Configuration')

def table_creator():
    return jpy.get_type('io.deephaven.engine.table.impl.TableCreatorImpl').INSTANCE

def get_now_table():
    return Table(j_table=table_creator().emptyTable(1)).select("Timestamp=DateTime.now()")

def get_trades_stream():
    return kt.consume(
        { 'bootstrap.servers' : configuration().bootstrapServers(),
          'schema.registry.url' : configuration().schemaRegistryUrl() },
        configuration().tradesTopic(),
        # https://github.com/deephaven/deephaven-core/issues/2305
        # value_spec = kt.avro_spec(configuration().tradeSchema()),
        value_spec = kt.avro_spec(configuration().tradesTopicSchemaName()),
        offsets=kt.ALL_PARTITIONS_SEEK_TO_BEGINNING,
        table_type=TableType.Stream)

def get_quotes_stream():
    return kt.consume(
        { 'bootstrap.servers' : configuration().bootstrapServers(),
          'schema.registry.url' : configuration().schemaRegistryUrl() },
        configuration().quotesTopic(),
        # https://github.com/deephaven/deephaven-core/issues/2305
        # value_spec = kt.avro_spec(configuration().quoteSchema()),
        value_spec = kt.avro_spec(configuration().quotesTopicSchemaName()),
        table_type=TableType.Stream)

def cast(j_table):
    # jpy.cast is necessary due to loss of type information due to generics
    # https://github.com/deephaven/deephaven-core/issues/2329
    return Table(j_table=jpy.cast(j_table, Table.j_object_type))

def get_trades_summary(trades, usd_prices):
    return cast(ops().getTradesSummary(trades.j_table, usd_prices.j_table))

def get_quotes_latest(quotes):
    return cast(ops().getQuotesLatest(quotes.j_table))

def get_trades_latest(trades):
    return cast(ops().getTradesLatest(trades.j_table))

def get_usd_prices(quotes_latest):
    return cast(ops().getUsdPrices(table_creator(), quotes_latest.j_table))
