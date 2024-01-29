package pres

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking

fun main() {
    for (k in 1..10) {
        timed("kotlin") {
            runBlocking {
                val f = flow {
                    for (i in 1..max) {
                        emit(i)
                    }
                }
                val sum = f.reduce { a, b -> a + b }
                assert(sum == sumUpTo(max))
            }
        }
    }
}
