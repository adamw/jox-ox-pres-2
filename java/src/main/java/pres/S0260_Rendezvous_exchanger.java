package pres;

import java.util.concurrent.Exchanger;

import static pres.Util.*;

public class S0260_Rendezvous_exchanger {
    public void test() throws Exception {
        timed("rendezvous+sync queue", () -> {
            Exchanger<Integer> data = new Exchanger<>();

            Thread t1 = Thread.ofVirtual().start(() -> {
                int i = 0;
                while (i <= MAX) {
                    try {
                        data.exchange(i);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    i += 1;
                }
            });

            Thread t2 = Thread.ofVirtual().start(() -> {
                long acc = 0L;
                for (int i = 0; i <= MAX; i++) {
                    try {
                        acc += data.exchange(-1);
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
        for (int i = 0; i < 19; i++) {
            new S0260_Rendezvous_exchanger().test();
        }
    }
}
