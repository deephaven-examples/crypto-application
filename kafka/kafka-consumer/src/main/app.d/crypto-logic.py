import jpy
from deephaven import KafkaTools as kt

def ops():
    return jpy.get_type('io.deephaven.crypto.Ops')

def configuration():
    return jpy.get_type('io.deephaven.crypto.kafka.Configuration')

def table_creator():
    return jpy.get_type('io.deephaven.db.tables.TableCreatorImpl').INSTANCE

def get_now_table():
    return table_creator().emptyTable(1).select("Timestamp=DBDateTime.now()")

def get_trades_stream():
    return kt.consumeToTable(
        { 'bootstrap.servers' : configuration().bootstrapServers(),
          'schema.registry.url' : configuration().schemaRegistryUrl() },
        configuration().tradesTopic(),
        key = kt.IGNORE,
        value = kt.avro(configuration().tradeSchema()),
        table_type='stream')\
        .dropColumns("KafkaPartition", "KafkaOffset", "KafkaTimestamp")

def get_quotes_stream():
    return kt.consumeToTable(
        { 'bootstrap.servers' : configuration().bootstrapServers(),
          'schema.registry.url' : configuration().schemaRegistryUrl() },
        configuration().quotesTopic(),
        key = kt.IGNORE,
        value = kt.avro(configuration().quoteSchema()),
        table_type='stream')\
        .dropColumns("KafkaPartition", "KafkaOffset", "KafkaTimestamp")

def get_trades_summary(trades, usd_prices):
    return ops().getTradesSummary(trades, usd_prices)

def get_quotes_latest(quotes):
    return ops().getQuotesLatest(quotes)

def get_trades_latest(trades):
    return ops().getTradesLatest(trades)

def get_usd_prices(quotes_latest):
    return ops().getUsdPrices(table_creator(), quotes_latest)
