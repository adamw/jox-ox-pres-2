package pres

import ox.channels.{Source, StageCapacity}
import ox.supervised

def testUsingOx(buffer: Int): Unit =
  timed(s"ox($buffer)") {
    supervised {
      given StageCapacity = StageCapacity(buffer)

      val source: Source[Int] = Source.range(0, Max, 1)
      val sum = source.fold(0L)(_ + _)

      assert(sum == sumUpTo(Max))
    }
  }

@main def runUsingOx(): Unit =
  for (k <- 1 to 10) {
    testUsingOx(0)
    testUsingOx(100)
    testUsingOx(1000)
  }
