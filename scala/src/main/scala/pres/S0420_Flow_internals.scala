package pres

object S0420_Flow_internals:

  class Flow[+T](val last: FlowStage[T])

  trait FlowStage[+T]:
    def run(emit: FlowEmit[T]): Unit

  trait FlowEmit[-T]:
    def apply(t: T): Unit

  /*

  Signal completion: when `run` completes

  Signal error: throw an exception

   */

end S0420_Flow_internals
