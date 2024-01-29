package pres;

import org.apache.pekko.actor.ActorSystem;
import org.apache.pekko.japi.Pair;
import org.apache.pekko.stream.javadsl.Source;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class S0040_Akka_streams_example {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var system = ActorSystem.create("streams");
        var delayed = CompletableFuture.delayedExecutor(5L, TimeUnit.SECONDS);

        var nats = Source.unfold(0, i -> Optional.of(Pair.create(i+1, i+1)));

        Source.range(1, 100)
                .throttle(1, Duration.ofSeconds(1))
                .mapAsync(4, i -> CompletableFuture.supplyAsync(() -> i * 3, delayed))
                .filter(i -> i % 2 == 0)
                .zip(nats)
                .runForeach(System.out::println, system)
                .toCompletableFuture()
                .get();

        system.terminate();
    }
}
