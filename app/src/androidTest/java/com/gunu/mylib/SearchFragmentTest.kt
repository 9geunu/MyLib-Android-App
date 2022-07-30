package com.gunu.mylib

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.ui.search.SearchFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    private lateinit var repository: FakeRepository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.repository = repository
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun testSearchBook() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "Effective Java, 3rd Edition",
                            subtitle = "",
                            isbn13 = 9780134685991,
                            price = "$38.00",
                            image = "image",
                            url = "url",
                            isBookmarked = false
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_search)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.search_book)).perform(click())

        onView(withId(R.id.search_book)).perform(typeText("Effective Java"))

        onView(withId(R.id.search_book)).perform(pressImeActionButton())

        runBlocking {
            delay(2_000)
        }

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("isbn13:9780134685991")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationSearchFragmentToDetailBookActivity() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "Effective Java, 3rd Edition",
                            subtitle = "",
                            isbn13 = 9780134685991,
                            price = "$38.00",
                            image = "image",
                            url = "url",
                            isBookmarked = false
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_search)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.search_book)).perform(click())

        onView(withId(R.id.search_book)).perform(typeText("Effective Java"))

        onView(withId(R.id.search_book)).perform(pressImeActionButton())

        runBlocking {
            delay(2_000)
        }

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("isbn13:9780134685991")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))

        onView(withText("Effective Java, 3rd Edition")).perform(click())

        Assert.assertEquals(navController.currentDestination?.id, R.id.navigation_detailBook)
    }

    @Test
    fun testEmptyResult() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "Effective Java, 3rd Edition",
                            subtitle = "",
                            isbn13 = 9780134685991,
                            price = "$38.00",
                            image = "image",
                            url = "url",
                            isBookmarked = false
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_search)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.search_book)).perform(click())

        onView(withId(R.id.search_book)).perform(typeText("Absolute C++"))

        onView(withId(R.id.search_book)).perform(pressImeActionButton())

        runBlocking {
            delay(2_000)
        }

        onView(withId(R.id.search_empty_image)).check(matches(isDisplayed()))
        onView(withId(R.id.search_empty_message)).check(matches(isDisplayed()))
    }

    @Test
    fun testError_networkDisconnected() {
        Assert.assertTrue(!repository.isNetworkConnected())

        runBlocking {
            repository.insertBook(
                    Book(
                            title = "Effective Java, 3rd Edition",
                            subtitle = "",
                            isbn13 = 9780134685991,
                            price = "$38.00",
                            image = "image",
                            url = "url",
                            isBookmarked = false
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<SearchFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_search)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.search_book)).perform(click())

        onView(withId(R.id.search_book)).perform(typeText("Absolute C++"))

        onView(withId(R.id.search_book)).perform(pressImeActionButton())

        runBlocking {
            delay(2_000)
        }

        onView(withId(R.id.search_error_image)).check(matches(isDisplayed()))
        onView(withId(R.id.search_error_message)).check(matches(isDisplayed()))
    }
}