package com.andcourse.micki.android_course_modules

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCurrenciesList() {
        val mainActivity = MainActivity()
        val currenciesList = mainActivity.getCurrenciesList()

        assertTrue(currenciesList.isNotEmpty())
    }
}
