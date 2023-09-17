package com.udacity.project4.ui.locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.domain.location.LocationUseCase
import com.udacity.project4.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocationListViewModelTest {

    private val locationUseCase = mock<LocationUseCase>()
    private val locationListViewModel = LocationListViewModel(locationUseCase)
    private val location = Location(
        id = 15,
        title = "Test",
        description = "description",
        locationName = "locationName",
        longitude = 3.2,
        latitude = 3.4
    )

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun test() = runTest(mainCoroutineRule.testDispatcher) {
        whenever(locationUseCase.getLocations()).thenReturn(
            flowOf(
                listOf(
                    location, location.copy(id = 11), location.copy(id = 12)
                )
            )
        )

        locationListViewModel.getLocation()
        verify(locationUseCase).getLocations()

        val actual = locationListViewModel.locations.getOrAwaitValue()
        assertThat(actual.size, `is`(3))
    }

    private fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
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

        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            this.removeObserver(observer)
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}
