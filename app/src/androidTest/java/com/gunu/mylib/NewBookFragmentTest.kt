package com.gunu.mylib

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.ui.newbook.NewBookFragment
import com.gunu.mylib.ui.search.SearchFragment
import junit.framework.Assert.assertEquals
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
class NewBookFragmentTest {

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
    fun testNewBookFragment() {
        runBlocking {
            repository.insertBook(
                Book(
                title = "title",
                subtitle = "subtitle",
                isbn13 = 1,
                price = "price",
                image = "image",
                url = "url",
                isBookmarked = false
            ))
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<NewBookFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_newbook)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:1")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationNewFragmentToDetailBookActivity() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "title",
                            subtitle = "subtitle",
                            isbn13 = 1,
                            price = "price",
                            image = "image",
                            url = "url",
                            isBookmarked = false
                    ))
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<NewBookFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_newbook)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withText("title")).perform(click())
        assertEquals(navController.currentDestination?.id, R.id.navigation_detailBook)

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
                    ))
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val newFrag = launchFragmentInContainer<NewBookFragment>(Bundle(), R.style.AppTheme)

        newFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_newbook)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.new_error_image)).check(matches(isDisplayed()))
        onView(withId(R.id.new_error_message)).check(matches(isDisplayed()))
    }
}