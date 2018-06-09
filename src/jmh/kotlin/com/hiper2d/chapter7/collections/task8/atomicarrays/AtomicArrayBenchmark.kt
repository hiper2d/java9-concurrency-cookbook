package com.hiper2d.chapter7.collections.task8.atomicarrays

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit


@State(Scope.Thread)
@BenchmarkMode(value = [(Mode.AverageTime)])
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class AtomicArrayBenchmark {

    @Benchmark
    fun runThreadsBenchmark() = runWithThreads()

    @Benchmark
    fun runCoroutinesBenchmark() = runWithCoroutines()
}
