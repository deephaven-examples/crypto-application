import jpy

ApplicationState = jpy.get_type("io.deephaven.appmode.ApplicationState")

def crypto_application(app: ApplicationState):
    start_time = get_now_table()

    quotes_stream = get_quotes_stream()
    quotes_latest = get_quotes_latest(quotes_stream)

    usd_prices = get_usd_prices(quotes_latest)

    trades_stream = get_trades_stream()
    trades_latest = get_trades_latest(trades_stream)
    trades_summary = get_trades_summary(trades_stream, usd_prices)

    app.setField("start_time", start_time)
    app.setField("usd_prices", usd_prices)
    app.setField("trades_stream", trades_stream)
    app.setField("trades_latest", trades_latest)
    app.setField("trades_summary", trades_summary)
    app.setField("quotes_stream", quotes_stream)
    app.setField("quotes_latest", quotes_latest)
