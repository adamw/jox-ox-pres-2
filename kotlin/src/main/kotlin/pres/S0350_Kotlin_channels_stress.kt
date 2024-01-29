package pres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun testStress(buffer: Int) {
    timed("channels($buffer)") {
        runBlocking {
            val chainLength = 10_000;
            val elements = 10_000

            // create an array of channelCount channels
            val channels = Array(chainLength) { Channel<Long>(buffer) }

            launch(Dispatchers.Default) {
                var ch = channels[0]
                for (x in 1..elements) ch.send(63)
            }

            for (t in 1 until chainLength) {
                val ch1 = channels[t - 1]
                val ch2 = channels[t]
                launch(Dispatchers.Default) {
                    for (x in 1..elements) ch2.send(ch1.receive())
                }
            }

            launch(Dispatchers.Default) {
                var ch = channels[chainLength - 1]
                for (x in 1..elements) ch.receive()
            }
        }
    }
}

fun main() {
    for (k in 1..10) {
        testStress(100)
    }
}
