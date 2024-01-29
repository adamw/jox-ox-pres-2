package pres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun test(buffer: Int) {
    timed("channels($buffer)") {
        runBlocking {
            val channel = Channel<Long>(buffer)
            launch(Dispatchers.Default) {
                for (x in 1..max) channel.send(x)
                channel.close()
            }

            launch(Dispatchers.Default) {
                var sum = 0L
                for (y in channel) sum += y
                assert(sum == sumUpTo(max))
            }
        }
    }
}

fun main() {
    for (k in 1..10) {
        test(0)
        test(100)
        test(1000)
    }
}
