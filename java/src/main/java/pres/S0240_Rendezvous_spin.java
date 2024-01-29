package pres;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static pres.Util.MAX;
import static pres.Util.timed;

public class S0240_Rendezvous_spin {
    public void test(int spinIterations) throws Exception {
        var waiting = new AtomicReference<ThreadAndCell>();
        timed("rendezvous+spin (" + spinIterations + ")", () -> {
            Thread t1 = Thread.ofVirtual().start(() -> {
                Thread ourThread = Thread.currentThread();

                for (int i = 0; i <= MAX; i++) {
                    AtomicReference<Integer> ourCell = new AtomicReference<>(i);

                    if (waiting.compareAndSet(null, new ThreadAndCell(ourThread, ourCell))) {
                        // CAS was successful, we are the first thread: parking and waiting for the data to be consumed
                        int doSpin = spinIterations;
                        while (ourCell.get() != -1) {
                            if (doSpin > 0) {
                                Thread.onSpinWait();
                                doSpin -= 1;
                            } else {
                                LockSupport.park();
                            }
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
                        int doSpin = spinIterations;
                        while (ourCell.get() == -1) {
                            if (doSpin > 0) {
                                Thread.onSpinWait();
                                doSpin -= 1;
                            } else {
                                LockSupport.park();
                            }
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

    private long sumUpTo(int max) {
        return ((long) max * (max + 1)) / 2;
    }

    private record ThreadAndCell(Thread thread, AtomicReference<Integer> cell) {}

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 25; i++) {
            new S0240_Rendezvous_spin().test(100);
            new S0240_Rendezvous_spin().test(1000);
            new S0240_Rendezvous_spin().test(10000);
            System.out.println();
        }
    }
}
