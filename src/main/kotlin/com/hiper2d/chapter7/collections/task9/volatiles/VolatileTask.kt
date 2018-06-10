package com.hiper2d.chapter7.collections.task9.volatiles

import com.hiper2d.logger

class VolatileTask(val flag: VolatileFlag): Runnable {
    companion object {
        val log = logger<VolatileTask>()
    }

    override fun run() {
        var i = 0
        while (flag.flag) {
            i++
        }
        log.debug("Stopped")
    }
}