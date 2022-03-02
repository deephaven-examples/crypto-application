import jpy
from typing import Callable

def initialize_implicitly(func: Callable[[], None]):
    app = jpy.get_type("io.deephaven.appmode.ApplicationContext").get()
    new_globals = dict(globals())
    exec(func.__code__, new_globals)
    for key, value in new_globals.items():
        if not key in globals().keys() or globals()[key] != value:
            app.setField(key, value)
            globals()[key] = value

initialize_implicitly(crypto_start)
