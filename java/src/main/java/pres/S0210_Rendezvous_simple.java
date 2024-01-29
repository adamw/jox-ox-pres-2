package pres;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static pres.Util.*;

public class S0210_Rendezvous_simple {
    private final AtomicReference<ThreadAndCell> waiting = new AtomicReference<>();

    public void test() throws Exception {
        timed("rendezvous", () -> {
            Thread t1 = Thread.ofVirtual().start(() -> {
                Thread ourThread = Thread.currentThread();

                for (int i = 0; i <= MAX; i++) {
                    AtomicReference<Integer> ourCell = new AtomicReference<>(i);

                    if (waiting.compareAndSet(null, new ThreadAndCell(ourThread, ourCell))) {
                        // CAS was successful, we are the first thread: parking and waiting for the data to be consumed
                        while (ourCell.get() != -1) {
                            LockSupport.park();
                        }
                    } else {
                        // CAS was unsuccessful, there is already a thread waiting for us: clearing `waiting` for the
                        // next iteration, sending the data using the provided cell and unparking the other thread
                        ThreadAndCell other = waiting.get();
                        waiting.set(null);

                        other.cell.set(i);

                        LockSupport.unpark(other.thread);
                    }
                }
            });

            Thread t2 = Thread.ofVirtual().start(() -> {
                long acc = 0L;
                Thread ourThread = Thread.currentThread();

                for (int i = 0; i <= MAX; i++) {
                    AtomicReference<Integer> ourCell = new AtomicReference<>(-1); // -1 -> no data provided yet
                    if (waiting.compareAndSet(null, new ThreadAndCell(ourThread, ourCell))) {
                        // CAS was successful, we are the first thread: parking and waiting for the data to be provided
                        while (ourCell.get() == -1) {
                            LockSupport.park();
                        }
                        acc += ourCell.get();
                    } else {
                        // CAS was unsuccessful, there is already a thread waiting for us: clearing `waiting` for the
                        // next iteration, consuming the data and unparking the other thread
                        ThreadAndCell other = waiting.get();
                        waiting.set(null);

                        acc += other.cell.get();
                        other.cell.set(-1);

                        LockSupport.unpark(other.thread);
                    }
                }

                assert acc == sumUpTo(MAX);
            });

            t1.join();
            t2.join();

            return null;
        });
    }

    private record ThreadAndCell(Thread thread, AtomicReference<Integer> cell) {}

    public static void main(String[] args) throws Exception {
        for (int i=0; i<10; i++) {
            new S0210_Rendezvous_simple().test();
        }
    }
}
