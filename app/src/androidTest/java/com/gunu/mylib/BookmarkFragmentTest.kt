package com.gunu.mylib

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository
import com.gunu.mylib.ui.bookmark.BookmarkFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookmarkFragmentTest {

    private lateinit var repository: IRepository

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
    fun testBookmarkFragment() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "title",
                            subtitle = "subtitle",
                            isbn13 = 1,
                            price = "price",
                            image = "image",
                            url = "url",
                            isBookmarked = true
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val bookmarkFrag = launchFragmentInContainer<BookmarkFragment>(Bundle(), R.style.AppTheme)

        bookmarkFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_bookmark)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.bookmark_toggle_button)).check(matches(isChecked()))
        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:1")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testUnBookmark() {
        runBlocking {
            repository.insertBook(
                    Book(
                            title = "title",
                            subtitle = "subtitle",
                            isbn13 = 1,
                            price = "price",
                            image = "image",
                            url = "url",
                            isBookmarked = true
                    )
            )
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val bookmarkFrag = launchFragmentInContainer<BookmarkFragment>(Bundle(), R.style.AppTheme)

        bookmarkFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_bookmark)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.bookmark_toggle_button)).check(matches(isChecked()))
        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:1")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))

        onView(withId(R.id.bookmark_toggle_button)).perform(click())

        runBlocking {
            val list = repository.observeBookmarkedBooks().first()

            Assert.assertEquals(0, list.size)
        }
    }

    @Test
    fun testEmptyBookmarkList() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val bookmarkFrag = launchFragmentInContainer<BookmarkFragment>(Bundle(), R.style.AppTheme)

        bookmarkFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_bookmark)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.bookmark_empty_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_empty_message)).check(matches(isDisplayed()))
    }
}