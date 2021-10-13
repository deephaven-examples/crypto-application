# Crypto Application

### About

The crypto application is a multifaceted example that extends the [Deephaven Community Core](https://github.com/deephaven/deephaven-core) docker images.

The [publisher](publisher) connects directly to a number of cryptocurrency exchanges via the [XChange](https://github.com/knowm/XChange)
library, and publishes quotes and trades into a [Redpanda](https://vectorized.io/redpanda) [Kafka server](redpanda).

The [Deephaven server](grpc-api) consumes the Kafka feed, and creates a number of useful tables from the cryptocurrency quotes and trades.

The [web](web) server packages up a specific layout for the cryptocurrency tables.

The [client](client) is a stand-alone client that demonstrates pulling the table data from Deephaven into a remote application.

### Launch

To launch the `latest` version (updated every release), run:

```shell
$ docker-compose up -d
```

To launch the `edge` version (updated every commit to the main branch), or a version built locally, run:

```shell
$ TAG=edge docker-compose up -d
```

These will launch a self-contained environment, and exposes port 10042 by default, [http://localhost:10042/ide/](http://localhost:10042/ide/).

To change the defaults, see the [environment file](.env).

The stand-alone client is not started by default. To see it in action, you can run:

```shell
$ docker-compose up client
```

### Development

To build:

```shell
$ ./gradlew clean build
```

To develop against an unreleased version of the Deephaven core images, change the base images as appropriate in [gradle.properties](gradle.properties).
