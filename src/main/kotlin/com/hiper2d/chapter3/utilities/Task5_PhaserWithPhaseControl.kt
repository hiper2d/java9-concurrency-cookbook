package com.hiper2d.chapter3.utilities

import java.time.LocalDateTime
import java.util.concurrent.Phaser
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class MyPhaser: Phaser() {

    override fun onAdvance(phase: Int, registeredParties: Int): Boolean {
        return when (phase) {
            0 -> studentsArrived()
            1 -> finishFirstExercise()
            2 -> finishSecondExercise()
            3 -> finishExam()
            else -> true
        }
    }

    private fun studentsArrived(): Boolean {
        println("Phaser: The exam are going to start. The students are ready.")
        println("Phaser: We have $registeredParties students.")
        return false
    }

    private fun finishFirstExercise(): Boolean {
        println("Phaser: All the students have finished the first exercise.")
        println("Phaser: It's time for the second one.")
        return false
    }

    private fun finishSecondExercise(): Boolean {
        println("Phaser: All the students have finished the second exercise.")
        println("Phaser: It's time for the third one.")
        return false
    }

    private fun finishExam(): Boolean {
        println("Phaser: All the students have finished the exam.")
        println("Phaser: Thank you for your time.")
        return false
    }
}

class Student(private val phaser: Phaser): Runnable {
    override fun run() {
        println("${Thread.currentThread().name} Has arrived to do the exam. ${LocalDateTime.now()}")
        phaser.arriveAndAwaitAdvance()

        println("${Thread.currentThread().name} Is going to do the first exercise. ${LocalDateTime.now()}")
        doExercise1()
        println("${Thread.currentThread().name} Has done the first exercise. ${LocalDateTime.now()}")
        phaser.arriveAndAwaitAdvance()

        println("${Thread.currentThread().name} Is going to do the second exercise. ${LocalDateTime.now()}")
        doExercise2()
        println("${Thread.currentThread().name} Has done the second exercise. ${LocalDateTime.now()}")
        phaser.arriveAndAwaitAdvance()

        println("${Thread.currentThread().name} Is going to do the third exercise. ${LocalDateTime.now()}")
        doExercise3()
        println("${Thread.currentThread().name} Has finished the exam. ${LocalDateTime.now()}")
        phaser.arriveAndAwaitAdvance()
    }

    private fun doExercise1() {
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextLong(10))
    }

    private fun doExercise2() {
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextLong(10))
    }

    private fun doExercise3() {
        TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextLong(10))
    }
}

fun main(args: Array<String>) {
    val phaser = MyPhaser()

    val studentThreads = Array(5) {
        val thr = Thread(Student(phaser), "Student$it")
        phaser.register()
        thr.start()
        thr
    }

    studentThreads.forEach {
        it.join()
    }

    println("Main: The phaser has finished: ${phaser.isTerminated}")
}