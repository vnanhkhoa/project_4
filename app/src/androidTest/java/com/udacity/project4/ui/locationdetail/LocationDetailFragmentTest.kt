package com.udacity.project4.ui.locationdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.udacity.project4.data.database.entites.Location
import com.udacity.project4.di.appModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito.after
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
class LocationDetailFragmentTest : KoinTest {

    @JvmField
    @Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockNavController = mock(NavController::class.java)
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

    @Test
    fun test_display() {
        launchFragmentInContainer(
            LocationDetailFragmentArgs(location).toBundle(),
            R.style.LoacationRemindersTheme,
        ) {
            LocationDetailFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(
                            fragment.requireView(),
                            mockNavController
                        )
                    }
                }
            }
        }
        onView(withText(location.title)).check(matches(isDisplayed()))
        onView(withText(location.description)).check(matches(isDisplayed()))
        onView(withText(location.locationName)).check(matches(isDisplayed()))
    }

    @Test
    fun test_save() {
        launchFragmentInContainer(
            LocationDetailFragmentArgs(location).toBundle(),
            R.style.LoacationRemindersTheme,
        ) {
            LocationDetailFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        Navigation.setViewNavController(
                            fragment.requireView(),
                            mockNavController
                        )
                    }
                }
            }
        }

        onView(withId(R.id.btn_save)).perform(click())
        verify(mockNavController, after(1000)).popBackStack()
    }
}