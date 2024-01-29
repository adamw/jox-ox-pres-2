package pres

import ox.channels.{Channel, ChannelClosed, Source}
import ox.repeatWhile

def map1[T, U](ch: Source[T], f: T => U): Source[U] =
  val out = Channel[U]()
  Thread.ofVirtual().start(() => {
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
  })
  out

@main def mapAttempt1(): Unit =
  val ch = Channel[Int](5)
  val ch2 = map1(ch, n => n * 2)

  ch.send(10)
  println(ch2.receive())

  ch.send(43)
  println(ch2.receive())

@main def mapAttempt1Fail(): Unit =
  val ch = Channel[Int](5)
  val ch2 = map1(ch, n => throw new RuntimeException("Boom!"))

  ch.send(10)
  println(ch2.receive())
