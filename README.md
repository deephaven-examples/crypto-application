# Crypto Sources

### Prerequisites

Ensure local build of [deephaven-core](https://github.com/deephaven/deephaven-core):

```
deephaven-core$ ./gradlew prepareCompose
```

### Development build

```
$ ./gradlew kafka-publisher:dockerBuildImage kafka-consumer:buildDocker kafka-web:buildDocker
```

Note: when making changes to `deephaven-core`, you'll want to make sure you execute a clean build, as the `deephaven-core` changes don't cause a cache-invalidation in this project (yet):

```
./gradlew clean kafka-publisher:dockerBuildImage kafka-consumer:buildDocker kafka-web:buildDocker
```

### Development launch

```
$ docker-compose up
```

Starts a self-contained environment, and exposes port 10042, [http://localhost:10042/ide/](http://localhost:10042/ide/).

