package pres;

import java.util.concurrent.ConcurrentHashMap;

import static pres.Util.timed;

public class S0050_Loom {
    public static void main(String[] args) throws Exception {
        timed("loom", () -> {
            var threads = new Thread[10_000_000];
            var results = ConcurrentHashMap.newKeySet();
            for (int i=0; i<threads.length; i++) {
                threads[i] = Thread.startVirtualThread(() -> results.add(0));
            }

            for (Thread thread : threads) {
                thread.join();
            }
            return null;
        });
    }
}
