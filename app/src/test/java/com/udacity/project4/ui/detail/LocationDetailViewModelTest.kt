package com.udacity.project4.ui.detail

import com.google.android.gms.location.Geofence
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.domain.location.LocationUseCase
import com.udacity.project4.ui.locationdetail.LocationDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.BeforeClass
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify

@RunWith(Enclosed::class)
@ExperimentalCoroutinesApi
class LocationDetailViewModelTest {

    private companion object {
        val locationUseCase = mock<LocationUseCase>()
        val locationDetailViewModel = spy(LocationDetailViewModel(locationUseCase))
        val location = Location(
            id = 15,
            title = "Test",
            description = "description",
            locationName = "locationName",
            longitude = 3.2,
            latitude = 3.4
        )

        @BeforeClass
        @JvmStatic
        fun setUp() {
            Dispatchers.setMain(UnconfinedTestDispatcher())
        }
    }

    class SaveLocationTest {
        @Test
        fun test_isUpdate_true() = runTest(UnconfinedTestDispatcher()) {
            val expected = "expected"
            var actual = ""
            val callBack: (geofence: Geofence) -> Unit = {
                actual = expected
            }

            locationDetailViewModel.setLocation(location)
            locationDetailViewModel.saveLocation(callBack)

            verify(locationUseCase).update(any())
            assertThat(actual, `is`(expected))
        }

        @Test
        fun test_isUpdate_false() = runTest(UnconfinedTestDispatcher()) {
            val expected = "expected"
            var actual = ""
            val callBack = mock<((geofence: Geofence) -> Unit)> {
                actual = expected
            }

            locationDetailViewModel.saveLocation(callBack)

            verify(locationUseCase).create(any())
            assertThat(actual, `is`(expected))
        }
    }

    class DeleteLocation {
        @Test
        fun test() = runTest(UnconfinedTestDispatcher()) {
            locationDetailViewModel.setLocation(location)
            locationDetailViewModel.deleteLocation()

            verify(locationUseCase).delete(location)
        }
    }
}