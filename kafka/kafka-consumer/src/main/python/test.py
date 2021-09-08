import jpy
from deephaven import KafkaTools as kt
from deephaven import TableTools as tt

ApplicationState = jpy.get_type("io.deephaven.appmode.ApplicationState")
ApplicationContext = jpy.get_type("io.deephaven.appmode.ApplicationContext")
Configuration = jpy.get_type('io.deephaven.crypto.kafka.Configuration')

def get_now_table():
    return tt.emptyTable(1).select("Timestamp=DBDateTime.now()")

def get_trades_stream():
    return kt.consumeToTable(
        { 'bootstrap.servers' : Configuration.bootstrapServers(),
          'schema.registry.url' : Configuration.schemaRegistryUrl() },
        Configuration.tradesTopic(),
        key = kt.IGNORE,
        value = kt.avro(Configuration.tradeSchema()),
        table_type='stream')\
        .dropColumns("KafkaPartition", "KafkaOffset", "KafkaTimestamp")

def get_quotes_stream():
    return kt.consumeToTable(
        { 'bootstrap.servers' : Configuration.bootstrapServers(),
          'schema.registry.url' : Configuration.schemaRegistryUrl() },
        Configuration.quotesTopic(),
        key = kt.IGNORE,
        value = kt.avro(Configuration.quoteSchema()),
        table_type='stream')\
        .dropColumns("KafkaPartition", "KafkaOffset", "KafkaTimestamp")

def get_trades_sum(trades):
    return trades.view("instrument", "size").sumBy("instrument")

def crypto_application(app: ApplicationState):
    start_time = get_now_table()
    trades_stream = get_trades_stream()
    quotes_stream = get_quotes_stream()
    trades_sum = get_trades_sum(trades_stream)

    app.setField("start_time", start_time)
    app.setField("trades_stream", trades_stream)
    app.setField("quotes_stream", quotes_stream)
    app.setField("trades_sum", trades_sum)

#ApplicationContext.initialize(crypto_application)

app = ApplicationContext.get()
crypto_application(app)