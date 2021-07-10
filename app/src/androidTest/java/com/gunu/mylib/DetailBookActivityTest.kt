package com.gunu.mylib

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.ui.MainActivity
import com.gunu.mylib.ui.detail.DetailBookActivity
import com.gunu.mylib.ui.detail.DetailBookActivityArgs
import com.gunu.mylib.ui.newbook.NewBookFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailBookActivityTest {

    private lateinit var repository: IRepository

    @Before
    fun initRepository() {
        repository = FakeRepository()
        ServiceLocator.repository = repository

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
    }

    @ExperimentalCoroutinesApi
    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }


    @Test
    fun testDetailBookActivity() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:1")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isNotChecked()))

        mainActivityScenario.close()
    }

    @Test
    fun testBookmark() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:1")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isNotChecked()))

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).check(ViewAssertions.matches(ViewMatchers.isChecked()))

        runBlocking {
            repository.getBookByIsbn(1)?.isBookmarked?.let { Assert.assertTrue(it) }
        }

        mainActivityScenario.close()
    }

    @Test
    fun testUnBookmark() {
        runBlocking {
            repository.deleteBook(1)

            repository.insertBook(
                    Book(
                            title = "title",
                            subtitle = "subtitle",
                            isbn13 = 2,
                            price = "price",
                            image = "image",
                            url = "url",
                            isBookmarked = true
                    ))
        }

        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("title")).check(matches(isDisplayed()))
        onView(withText("subtitle")).check(matches(isDisplayed()))
        onView(withText("isbn13:2")).check(matches(isDisplayed()))
        onView(withText("price")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isChecked()))

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).check(ViewAssertions.matches(ViewMatchers.isNotChecked()))

        runBlocking {
            repository.getBookByIsbn(2)?.isBookmarked?.let { Assert.assertFalse(it) }
        }

        mainActivityScenario.close()
    }

}