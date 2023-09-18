package com.udacity.project4.ui.locations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.data.database.daos.LocationDao
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.di.appModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@MediumTest
class LocationListFragmentTest : KoinTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockNavController = Mockito.mock(NavController::class.java)
    private lateinit var locationDao: LocationDao
    private val location = Location(
        id = 15,
        title = "Test",
        description = "description",
        locationName = "locationName",
        longitude = 3.2,
        latitude = 3.4
    )

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(appModule)
        }
    }

    @After
    fun teaDown() {
        stopKoin()
    }

    // Test for display snack bar
    @Test
    fun test_display_no_data() {
        launchFragmentInContainer<LocationListFragment>(bundleOf(), R.style.LoacationRemindersTheme)
        onView(withText("No Data")).check(matches(isDisplayed()))
        onView(withText("Location not found")).check(matches(isDisplayed()))
    }

    @Test
    fun test_display_has_data() {
        runBlocking {
            locationDao.insert(location)
        }
        launchFragmentInContainer<LocationListFragment>(bundleOf(), R.style.LoacationRemindersTheme)
        onView(withText(location.title)).check(matches(isDisplayed()))
        onView(withText(location.locationName)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navigate_to_location_detail() {
        launchFragmentInContainer<LocationListFragment>(
            bundleOf(),
            R.style.LoacationRemindersTheme
        ).onFragment {
            Navigation.setViewNavController(
                it.requireView(),
                mockNavController
            )
        }

        onView(withId(R.id.btn_add)).perform(click())
        Mockito.verify(mockNavController)
            .navigate(LocationListFragmentDirections.actionLocationListFragmentToLocationDetailFragment())
    }
}