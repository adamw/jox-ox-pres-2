package pres

def timed[T](label: String)(t: => T): T =
  val start = System.currentTimeMillis()
  val r = t
  val end = System.currentTimeMillis()
  println(s"$label took: ${end - start} ms")
  r

def sumUpTo(n: Int): Long = n.toLong * (n + 1) / 2

val Max = 10_000_000