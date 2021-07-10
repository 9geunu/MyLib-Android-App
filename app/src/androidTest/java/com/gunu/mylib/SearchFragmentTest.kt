package com.gunu.mylib

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.MainActivity
import com.gunu.mylib.ui.search.SearchFragment
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

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
                    ))
        }

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.navigation_search)).perform(click()).check(matches(isDisplayed()))

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

        activityScenario.close()
    }
}