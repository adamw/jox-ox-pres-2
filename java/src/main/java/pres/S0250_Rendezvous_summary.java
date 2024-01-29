package pres;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

import static pres.Util.MAX;
import static pres.Util.timed;

public class S0250_Rendezvous_summary {
    /*

    We have a pretty good way to communicate two threads.
    Can be further enhanced by avoiding allocations & using VarHandles.
    Do VT matter?

    Is it slow?
    * on par with java's concurrent collections
      * SynchronousQueue uses Thread.yield
      * Exchanger uses busy-looping

    * comparing to the stream examples:
      * no buffers
      * are we measuring the same thing?

     */
}
