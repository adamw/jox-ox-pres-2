package pres

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() {
    for (i in 1..10) {
        timed("coroutines") {
            runBlocking {
                val waiting = AtomicReference<Any>(null) // Pair<Continuation<Unit>, Long> or Continuation<Long>

                launch(Dispatchers.Default) {
                    for (i in 1..max) {
                        suspendCoroutine<Unit> { ourContinuation ->
                            // if the CAS is successful, our coroutine gets suspended
                            if (!waiting.compareAndSet(null, ourContinuation to i)) {
                                val theirContinuation = waiting.get() as Continuation<Long>
                                waiting.set(null)

                                // if the CAS is not successful, we're resuming our continuation, as the other party is already waiting
                                theirContinuation.resume(i.toLong())
                                ourContinuation.resume(Unit)
                            }
                        }
                    }
                }

                launch(Dispatchers.Default) {
                    var sum = 0L

                    for (i in 1..max) {
                        val toAdd = suspendCoroutine<Long> { ourContinuation ->
                            if (!waiting.compareAndSet(null, ourContinuation)) {
                                val theirDataAndContinuation = waiting.get() as Pair<Continuation<Unit>, Long>
                                waiting.set(null)

                                theirDataAndContinuation.first.resume(Unit)
                                ourContinuation.resume(theirDataAndContinuation.second)
                            }
                        }
                        sum += toAdd
                    }

                    assert(sum == sumUpTo(max))
                }
            }
        }
    }
}
