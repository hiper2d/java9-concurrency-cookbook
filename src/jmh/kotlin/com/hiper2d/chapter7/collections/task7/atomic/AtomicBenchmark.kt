package com.hiper2d.chapter7.collections.task7.atomic

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@BenchmarkMode(value = [(Mode.AverageTime)])
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class AtomicBenchmark {

    @Benchmark
    fun runThreadsBenchmark() = runWithThreads()

    @Benchmark
    fun runCoroutinesBenchmark() = runWithCoroutines()
}