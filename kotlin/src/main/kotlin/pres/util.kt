package pres

inline fun <T> timed(label: String, block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()
    println("$label Took ${end - start}ms")
    return result
}

fun sumUpTo(n: Long): Long = n * (n + 1) / 2

val max = 10_000_000L
