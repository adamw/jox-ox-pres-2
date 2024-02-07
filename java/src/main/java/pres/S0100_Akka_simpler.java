package pres;

import org.apache.pekko.actor.ActorSystem;
import org.apache.pekko.japi.Pair;
import org.apache.pekko.stream.javadsl.Source;

import java.util.Optional;

import static pres.Util.*;

public class S0100_Akka_simpler {
    public static void main(String[] args) throws Exception {
        var system = ActorSystem.create("streams");

        try {
            for (int k=0; k<10; k++) {
                timed("akka", () -> {
                    var nats = Source
                            .unfold(0, i -> (i > MAX) ?
                                    Optional.empty() :
                                    Optional.of(Pair.create(i + 1, i + 1)));

                    var consumeWithFold = nats
                            .runFold(Long.valueOf(0), Long::sum, system)
                            .toCompletableFuture()
                            .get();

                    assert consumeWithFold == sumUpTo(MAX);
                    return null;
                });
            }
        } finally {
            system.terminate();
        }
    }
}
