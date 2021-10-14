# Crypto Application

### About

The crypto application is a multifaceted example that extends the [Deephaven Community Core](https://github.com/deephaven/deephaven-core) docker images.

The [publisher](publisher) connects directly to a number of cryptocurrency exchanges via the [XChange](https://github.com/knowm/XChange)
library, and publishes quotes and trades into a [Redpanda](https://vectorized.io/redpanda) [Kafka server](redpanda).

The [Deephaven server](grpc-api) consumes the Kafka feed, and creates a number of useful tables from the cryptocurrency quotes and trades.

The [web](web) server packages up a specific layout for the cryptocurrency tables.

The [client](client) is a stand-alone client that demonstrates pulling the table data from Deephaven into a remote application.

### Requirements

* [docker](https://www.docker.com/) or [docker-compatible](https://podman.io/) engine
* [docker-compose](https://docs.docker.com/compose/)

### Launch

To launch the latest release, you can clone the repository and run via:

```shell
$ git clone https://github.com/deephaven-examples/crypto-application.git
$ cd crypto-application
$ docker-compose up -d
```

This will launch a self-contained environment at [http://localhost:10042/ide/](http://localhost:10042/ide/).

You may download the release [docker-compose.yml](release/docker-compose.yml) file instead of cloning the repository if preferred:

```shell
$ mkdir crypto-application
$ cd crypto-application
$ curl https://raw.githubusercontent.com/deephaven-examples/crypto-application/main/release/docker-compose.yml -o docker-compose.yml
$ docker-compose up -d
```

The stand-alone client is not started by default. To see it in action, run:

```shell
$ docker-compose up client
```

### Cleanup

The default configuration creates a few named volumes. If you want to delete just the containers, run:

```shell
$ docker-compose down
```

If you want to delete the containers and the volumes, run:

```shell
$ docker-compose down -v
```

### Development

#### Requirements

* [Java](https://openjdk.java.net/) version between 8 and 16 is required to build. See [Gradle Compatibility Matrix](https://docs.gradle.org/7.2/userguide/compatibility.html). 
* [docker](https://www.docker.com/) or [docker-compatible](https://podman.io/) engine.

#### Build

```shell
$ ./gradlew clean build
```

#### Run

```shell
$ TAG=edge docker-compose up -d
```

To develop against an unreleased version of the Deephaven core images, change the base images as appropriate in [gradle.properties](gradle.properties).

To change the defaults, edit the [environment file](.env).
