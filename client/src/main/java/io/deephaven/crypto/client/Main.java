package io.deephaven.crypto.client;

import io.deephaven.client.impl.DaggerDeephavenFlightRoot;
import io.deephaven.client.impl.FlightSession;
import io.deephaven.client.impl.TableHandle;
import io.deephaven.qst.table.TableSpec;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.arrow.flight.FlightStream;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  private static TableSpec query() {
    return TableSpec.ticket("a/io.deephaven.crypto.Application/f/usd_prices")
        .where("Currency=`BTC`")
        .view("USD");
  }

  private static String queryDescription() {
    return "Get BTC/USD prices";
  }

  public static void main(String[] args) throws Exception {
    final String target = System.getenv("DEEPHAVEN_GRPC_API");
    log.info("Connecting to '{}'...", target);

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> onShutdown(scheduler, channel)));

    final FlightSession session =
        DaggerDeephavenFlightRoot.create()
            .factoryBuilder()
            .allocator(new RootAllocator())
            .scheduler(scheduler)
            .managedChannel(channel)
            .build()
            .newFlightSession();

    log.info("Executing query '{}'...", queryDescription());
    try (final TableHandle handle = session.session().batch().execute(query())) {
      for (int i = 0; i < 10; ++i) {
        try (final FlightStream stream = session.stream(handle)) {
          while (stream.next()) {
            final Float8Vector vector = (Float8Vector) stream.getRoot().getVector("USD");
            final int count = vector.getValueCount();
            for (int j = 0; j < count; ++j) {
              final double btcPrice = vector.get(j);
              System.out.printf("%.04f%n", btcPrice);
            }
          }
          Thread.sleep(1000);
        }
      }
    } finally {
      session.session().closeFuture().get(10, TimeUnit.SECONDS);
      session.close();
    }
    System.exit(0);
  }

  private static void onShutdown(
      ScheduledExecutorService scheduler, ManagedChannel managedChannel) {
    log.info("Shutting down...");

    scheduler.shutdownNow();
    managedChannel.shutdownNow();

    try {
      if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
        throw new RuntimeException("Scheduler not shutdown after 10 seconds");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return;
    }
    try {
      if (!managedChannel.awaitTermination(10, TimeUnit.SECONDS)) {
        throw new RuntimeException("Channel not shutdown after 10 seconds");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
