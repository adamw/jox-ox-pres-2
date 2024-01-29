package pres

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

@main def start(): Unit =
  for (k <- 1 to 10) {
    timed(s"monix") {
      val source: Observable[Long] = Observable.range(from = 0, until = Max + 1, step = 1)
      val consumeWithFold: Task[Long] = source.foldLeftL(0L) { case (sum, elem) => elem + sum }

      assert(consumeWithFold.runSyncUnsafe() == sumUpTo(Max))
    }
  }
