package com.hiper2d.chapter2.synchronization

import java.lang.Thread.sleep


class ParkingCash {
    companion object {
        private const val COST = 2L
    }
    private var cash = 0L

    @Synchronized fun vehiclePay() {
        cash += COST
    }

    fun close() {
        println("Closing accounting")
        synchronized(this) {
            println("Total amount os cash: $cash")
            cash = 0
        }
    }
}

class ParkingStats(
        var numberCars: Long = 0L,
        var numberMotorcycles: Long = 0L,
        val parkingCash: ParkingCash,
        val carControl: Any = Object(),
        val motoControl: Any = Object()
) {
    fun carComeIn() {
        synchronized(carControl) {
            numberCars++
        }
    }

    fun carGoOut() {
        synchronized(carControl) {
            numberCars--
        }
        parkingCash.vehiclePay()
    }

    fun motoComeIn() {
        synchronized(motoControl) {
            numberMotorcycles++
        }
    }

    fun motoGoOut() {
        synchronized(motoControl) {
            numberMotorcycles--
        }
        parkingCash.vehiclePay()
    }
}

class Sensor(val stats: ParkingStats): Runnable {
    override fun run() {
        (1..10).forEach {
            stats.carComeIn()
            stats.carComeIn()
            sleep(50)
            stats.motoComeIn()

            sleep(50)
            stats.motoGoOut()
            stats.carGoOut()
            stats.carGoOut()
        }
    }
}

fun main(args: Array<String>) {
    val parkingCash = ParkingCash()
    val parkingStats = ParkingStats(parkingCash = parkingCash)
    val numberOfSensors = Runtime.getRuntime().availableProcessors() * 2

    val threads = Array(numberOfSensors, arrayOfThreads(parkingStats))

    (1..numberOfSensors).forEachIndexed { i, _ ->
        threads[i].join()
    }

    println("Number of cars: ${parkingStats.numberCars}")
    println("Number of motocycles: ${parkingStats.numberMotorcycles}")
    parkingCash.close()
}

private fun arrayOfThreads(parkingStats: ParkingStats): (Int) -> Thread {
    return {
        val sensor = Sensor(stats = parkingStats)
        val tr = Thread(sensor)
        tr.start()
        tr
    }
}