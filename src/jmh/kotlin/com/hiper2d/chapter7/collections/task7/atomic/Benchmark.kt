package com.hiper2d.chapter7.collections.task7.atomic

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Mode

open class Benchmark {

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(value = [(Mode.AverageTime)])
    fun echo() {
        // reserved for performance test
        println("Hello")
    }
}