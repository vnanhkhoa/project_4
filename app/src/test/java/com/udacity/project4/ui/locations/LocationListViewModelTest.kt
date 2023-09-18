package com.udacity.project4.ui.locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.data.repository.location.LocationRepository
import com.udacity.project4.domain.location.LocationUseCaseImpl
import com.udacity.project4.ui.FakeDataSource
import com.udacity.project4.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class LocationListViewModelTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var locationListViewModel: LocationListViewModel

    private val location = Location(
        id = 15,
        title = "Test",
        description = "description",
        locationName = "locationName",
        longitude = 3.2,
        latitude = 3.4
    )

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        locationRepository = FakeDataSource()
        (locationRepository as FakeDataSource).deleteAll()
        locationListViewModel = LocationListViewModel(LocationUseCaseImpl(locationRepository))
    }

    @After
    fun teaDown() {
        (locationRepository as FakeDataSource).deleteAll()
        (locationRepository as FakeDataSource).setError(false)
    }

    @Test
    fun test_has_data() = runTest(mainCoroutineRule.testDispatcher) {
        locationRepository.add(location)
        locationListViewModel.getLocation()
        val actual = locationListViewModel.locations.getOrAwaitValue()
        assertThat(actual, notNullValue())
    }

    @Test
    fun test_no_data() = runTest(mainCoroutineRule.testDispatcher) {
        (locationRepository as FakeDataSource).setError(true)
        locationListViewModel.getLocation()
        val actual = locationListViewModel.showSnackBar.getOrAwaitValue()
        assertThat(actual, `is`("Location not found"))
    }
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2, timeUnit: TimeUnit = TimeUnit.SECONDS, afterObserve: () -> Unit = {}
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

    @Suppress("UNCHECKED_CAST") return data as T
}
