package pres

object S0530_Pros_and_cons {
  /*

   Akka/reactive:
   (+) versatile API
   (+) battle-proven implementation
   (-) difficult to create new stages
   (-) colored functions (Future / normal)
   (-) stack traces
   (-) additional runtime needed

   Kotlin:
   (+) performance
   (+) easy to create new stages
   (+) syntax (suspended functions)
   (+) straightforward APIs
   (-) colored functions (suspend / normal)
   (-) stack traces
   (-) compile-time transformations + an additional runtime needed

   Ox:
   (+) performance (channels)
   (+) easy to create new stages
   (+) no colored functions
   (+) stack traces
   (+) no additional runtime needed
   (+) functional/imperative APIs
   (-) functional/imperative APIs
   (-) in development, no integrations yet
   (-) not optimized
   (-) TBD

   All implementations are reactive:
   * back-pressured
   * error-handling
   * completion
   * asynchronous processing

   */
}
