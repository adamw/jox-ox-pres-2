package pres

object S0520_Pros_and_cons:
  ()

  /*

  Pros:
  * keeping the virtual threads principles: no coloring, meaningful stack traces, direct syntax
  * keeping the reactive streams principles: backpressure, async, error handling, completion
  * functional & imperative APIs, to implement custom stages (including go-like select-s!)
  * simple model: every stage is a separate thread
  * safety through "let if fail": structured concurrency

  Cons:
  * no event-loop benefits from automatic stage fusing
  * buffer between every stage - more coarse-grained
  * hot vs cold streams
  * different APIs/libraries for single-threaded / in-memory and asynchronous / IO streams

   */
