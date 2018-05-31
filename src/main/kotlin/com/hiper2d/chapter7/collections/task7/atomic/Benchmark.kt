package com.hiper2d.chapter7.collections.task7.atomic

import org.openjdk.jmh.annotations.Benchmark

class Benchmark {

    @Benchmark
    fun echo() {
        // reserved for performance test
        println("echo")
    }
}