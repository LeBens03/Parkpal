package com.example.parkpal

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Gets the value from a LiveData or waits for it to have one, with a timeout.
 * Useful for testing LiveData.
 *
 * @param time The maximum time to wait for the value.
 * @param timeUnit The time unit of the time parameter.
 * @return The LiveData value or null if not set within the timeout.
 * @throws TimeoutException if the value is not set within the timeout.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T? {
    var data: T? = null
    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData never sets a value.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set within $time $timeUnit.")
    }

    return data
}