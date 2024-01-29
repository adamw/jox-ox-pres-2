package pres;

import com.softwaremill.jox.Channel;

import static pres.Util.*;

public class S0340_Using_jox_stress {
    private static final int CHAIN_LENGTH = 10_000;

    public void test(int buffer) throws Exception {
        timed("jox (" + buffer + ")", () -> {
            int elements = 10_000;
            Channel<Integer>[] channels = new Channel[CHAIN_LENGTH];
            for (int i = 0; i < CHAIN_LENGTH; i++) {
                channels[i] = new Channel<>(buffer);
            }

            Thread[] threads = new Thread[CHAIN_LENGTH + 1];
            threads[0] = Thread.startVirtualThread(() -> {
                var ch = channels[0];
                for (int i = 0; i < elements; i++) {
                    try {
                        ch.send(63);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            for (int t = 1; t < CHAIN_LENGTH; t++) {
                int finalT = t;
                threads[t] = Thread.startVirtualThread(() -> {
                    var ch1 = channels[finalT - 1];
                    var ch2 = channels[finalT];
                    for (int i = 0; i < elements; i++) {
                        try {
                            ch2.send(ch1.receive());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }

            threads[CHAIN_LENGTH] = Thread.startVirtualThread(() -> {
                var ch = channels[CHAIN_LENGTH - 1];
                for (int i = 0; i < elements; i++) {
                    try {
                        ch.receive();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            for (Thread thread : threads) {
                thread.join();
            }

            return null;
        });
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new S0340_Using_jox_stress().test(100);
        }
    }
}
