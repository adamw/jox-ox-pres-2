package pres

import ox.channels.{Channel, ChannelClosed, Source}
import ox.{Ox, fork, forkDaemon, repeatWhile, supervised}

def map2[T, U](ch: Source[T], f: T => U)(using Ox): Source[U] =
  val out = Channel[U]()
  forkDaemon {
    repeatWhile {
      ch.receive() match
        case t: T @unchecked =>
          out.send(f(t))
          true
        case ChannelClosed.Done =>
          out.done()
          false
        case ChannelClosed.Error(e) =>
          out.error(e)
          false
    }
  }
  out

@main def mapAttempt2(): Unit =
  supervised {
    val ch = Channel[Int](5)
    val ch2 = map2(ch, n => n * 2)

    ch.send(10)
    println(ch2.receive())

    ch.send(43)
    println(ch2.receive())
  }

@main def mapAttempt2Fail(): Unit =
  supervised {
    val ch = Channel[Int](5)
    val ch2 = map2(ch, n => throw new RuntimeException("Boom!"))

    ch.send(10)
    println(ch2.receive())
  }
