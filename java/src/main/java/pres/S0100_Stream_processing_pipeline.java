package pres;

import org.apache.pekko.actor.ActorSystem;
import org.apache.pekko.japi.Pair;
import org.apache.pekko.stream.javadsl.Source;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class S0100_Stream_processing_pipeline {
    /*

    Can we do concurrent, fast, safe, blocking stream processing?

     */

    public static void main(String[] args) throws Exception {
        var system = ActorSystem.create("streams");
        Source.range(1, 100)
                .throttle(1, Duration.ofSeconds(1))
                .mapAsync(4,
                        i -> CompletableFuture
                                .supplyAsync(() -> i * 3, null)
                                .thenApplyAsync(k -> k + 1))
                .filter(i -> i % 2 == 0)
                .zip(Source.unfold(0, i -> Optional.of(Pair.create(i+1, i+1))))
                .runForeach(System.out::println, system);
    }

    /*

    What's in a stream processing pipeline?

    A. a number of processing stages, potentially concurrent
    B. an API to combine the stages into a pipeline
    C. a runtime which executes the pipeline

     */
}
