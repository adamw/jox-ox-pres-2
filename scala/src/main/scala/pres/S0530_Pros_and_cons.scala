package pres

object S0530_Pros_and_cons {
  /*

   Kotlin:
   (+) performance
   (+) single API for IO / computational streams
   (+) easy to create new stages
   (+) syntax (suspended functions)
   (-) colored functions (suspend / normal)
   (-) stack traces
   
   Akka/reactive:
   (+) versatile API
   (+) battle-proven implementation
   (-) difficult to create new stages
   (-) colored functions (Future / normal)
   (-) stack traces
   
   Ox:
   (+) straightforward API
   (+) no colored functions
   (+) stack traces
   (+) functional & imperative APIs, go-like select-s
   (+) error safety through "let it fail": structured concurrency
   (-) different libraries needed for IO / computational streams
   (-) no automatic stage fusing, no event-loop
   (-) buffer between every stage: more coarse-grained
   
   Other:
   * all implementations are reactive: back-pressured, with error-handling, completion, asynchronous processing
   * hot vs cold streams

   */
}