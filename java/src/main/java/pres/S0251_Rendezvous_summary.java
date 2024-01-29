package pres;

public class S0251_Rendezvous_summary {
    /*

    We have a pretty good way to communicate two threads.
    Can be further enhanced by avoiding allocations & using VarHandles.
    Do virtual threads matter?

    Is it slow?
    * on par with java's concurrent collections
      * SynchronousQueue uses Thread.yield
      * Exchanger uses busy-looping

    * comparing to the stream examples:
      * no buffers
      * are we measuring the same thing?

     */
}
