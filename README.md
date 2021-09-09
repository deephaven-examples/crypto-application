# Crypto Sources

### Prerequisites

Ensure local build of [deephaven-core](https://github.com/deephaven/deephaven-core):

```
deephaven-core$ ./gradlew prepareCompose
```

### Development build

```
$ ./gradlew kafka-publisher:dockerBuildImage kafka-consumer:buildDocker
```

### Development launch

```
$ docker-compose up
```

Starts a self-contained environment, and exposes port 10042, [http://localhost:10042/ide/](http://localhost:10042/ide/).

