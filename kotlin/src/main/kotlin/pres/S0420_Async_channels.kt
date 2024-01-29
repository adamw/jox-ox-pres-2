package pres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun asyncChannels(buffer: Int) {
    timed("channels($buffer)") {
        runBlocking {
            val max = 10_000_000L
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
        asyncChannels(0)
        asyncChannels(100)
        asyncChannels(1000)
    }
}
