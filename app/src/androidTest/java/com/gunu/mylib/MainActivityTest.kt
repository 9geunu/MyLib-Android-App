package com.gunu.mylib

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository
import com.gunu.mylib.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var repository: IRepository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.repository = repository

        runBlocking {
            repository.insertBook(
                    Book(
                            title = "Effective Java, 3rd Edition",
                            subtitle = "",
                            isbn13 = 9780134685991,
                            price = "$38.00",
                            image = "image",
                            url = "url",
                            isBookmarked = true
                    )
            )
        }
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun testNavigationToNewBook() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_newbook)).perform(click()).check(matches(isDisplayed()))

        onView(ViewMatchers.withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(ViewMatchers.withText("isbn13:9780134685991")).check(matches(isDisplayed()))
        onView(ViewMatchers.withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun testNavigationToSearch() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_search)).perform(click()).check(
            matches(isDisplayed()))

        onView(withId(R.id.search_book)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun testNavigationToBookmark() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_bookmark)).perform(click()).check(matches(isDisplayed()))

        onView(ViewMatchers.withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(ViewMatchers.withText("isbn13:9780134685991")).check(matches(isDisplayed()))
        onView(ViewMatchers.withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.open_url_button)).check(matches(isDisplayed()))
        onView(withId(R.id.book_image_view)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_toggle_button)).check(matches(isDisplayed()))

        activityScenario.close()
    }
}