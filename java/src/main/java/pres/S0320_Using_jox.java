package pres;

import com.softwaremill.jox.Channel;

import static pres.Util.*;

public class S0320_Using_jox {
    public void test(int buffer) throws Exception {
        timed("jox (" + buffer + ")", () -> {
            var data = new Channel<Integer>(buffer);

            Thread t1 = Thread.ofVirtual().start(() -> {
                int i = 0;
                while (i <= MAX) {
                    try {
                        data.send(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    i += 1;
                }
                data.done();
            });

            Thread t2 = Thread.ofVirtual().start(() -> {
                var acc = 0L;
                var repeat = true;
                while (repeat) {
                    try {
                        switch (data.receiveSafe()) {
                            case Integer i -> acc += i;
                            default -> repeat = false;
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                assert acc == sumUpTo(MAX);
            });

            t1.join();
            t2.join();

            return null;
        });
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new S0320_Using_jox().test(0);
            new S0320_Using_jox().test(100);
            new S0320_Using_jox().test(1000);
        }
    }
}
