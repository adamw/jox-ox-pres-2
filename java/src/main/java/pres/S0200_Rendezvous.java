package pres;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class S0200_Rendezvous {
    /*

    When implementing the above example with blocking threads, we have two stages:
    * producing the natural numbers
    * summing them up

    Which we'd like to run concurrently / asynchronously.

    In our Loom implementation, each stage = one virtual thread.
    The core part is passing data between two threads.

     */
}
