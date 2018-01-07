package com.hiper2d

import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import java.util.*

import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.*
import java.time.LocalDate

class UtilKtTest {

    @Test
    fun randomLocalDateGeneratorCorrectness() {
        val startDate = LocalDate.of(1970, 1, 1)
        val endDate = LocalDate.now()
        (1..100).forEach {
            assertThat(randomLocalDate(), allOf(greaterThanOrEqualTo(startDate), lessThanOrEqualTo(endDate)))
        }
    }
}
