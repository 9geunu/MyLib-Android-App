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
import com.gunu.mylib.ui.search.SearchFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailBookActivityTest {

    private lateinit var repository: FakeRepository

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

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("Joshua Bloch")).check(matches(isDisplayed()))
        onView(withText("Addison-Wesley")).check(matches(isDisplayed()))
        onView(withText("English")).check(matches(isDisplayed()))
        onView(withText("0134685997")).check(matches(isDisplayed()))
        onView(withText("9780134685991")).check(matches(isDisplayed()))
        onView(withText("416")).check(matches(isDisplayed()))
        onView(withText("2017")).check(matches(isDisplayed()))
        onView(withText("4/5")).check(matches(isDisplayed()))
        onView(withText("Java has changed dramatically since the previous edition of Effective Java was published shortly after the release of Java 6. This Jolt award-winning classic has now been thoroughly updated to take full advantage of the latest language and library features. The support in modern Java for multiple pa...")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))

        onView(withId(R.id.memo_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isNotChecked()))

        mainActivityScenario.close()
    }

    @Test
    fun testBookmark() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("Joshua Bloch")).check(matches(isDisplayed()))
        onView(withText("Addison-Wesley")).check(matches(isDisplayed()))
        onView(withText("English")).check(matches(isDisplayed()))
        onView(withText("0134685997")).check(matches(isDisplayed()))
        onView(withText("9780134685991")).check(matches(isDisplayed()))
        onView(withText("416")).check(matches(isDisplayed()))
        onView(withText("2017")).check(matches(isDisplayed()))
        onView(withText("4/5")).check(matches(isDisplayed()))
        onView(withText("Java has changed dramatically since the previous edition of Effective Java was published shortly after the release of Java 6. This Jolt award-winning classic has now been thoroughly updated to take full advantage of the latest language and library features. The support in modern Java for multiple pa...")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))

        onView(withId(R.id.memo_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isNotChecked()))

        onView(withId(R.id.bookmark_button)).perform(click())

        onView(withId(R.id.bookmark_button)).check(matches(isChecked()))

        runBlocking {
            repository.getBookByIsbn(1)?.isBookmarked?.let { Assert.assertTrue(it) }
        }

        mainActivityScenario.close()
    }

    @Test
    fun testUnBookmark() {
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
                    ))
        }

        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("Joshua Bloch")).check(matches(isDisplayed()))
        onView(withText("Addison-Wesley")).check(matches(isDisplayed()))
        onView(withText("English")).check(matches(isDisplayed()))
        onView(withText("0134685997")).check(matches(isDisplayed()))
        onView(withText("9780134685991")).check(matches(isDisplayed()))
        onView(withText("416")).check(matches(isDisplayed()))
        onView(withText("2017")).check(matches(isDisplayed()))
        onView(withText("4/5")).check(matches(isDisplayed()))
        onView(withText("Java has changed dramatically since the previous edition of Effective Java was published shortly after the release of Java 6. This Jolt award-winning classic has now been thoroughly updated to take full advantage of the latest language and library features. The support in modern Java for multiple pa...")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))

        onView(withId(R.id.memo_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isChecked()))

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.bookmark_button)).check(ViewAssertions.matches(ViewMatchers.isNotChecked()))

        runBlocking {
            repository.getBookByIsbn(9780134685991)?.isBookmarked?.let { Assert.assertFalse(it) }
        }

        mainActivityScenario.close()
    }

    @Test
    fun testMemo() {
        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.book_open_area)).perform(click())

        onView(withText("Effective Java, 3rd Edition")).check(matches(isDisplayed()))
        onView(withText("Joshua Bloch")).check(matches(isDisplayed()))
        onView(withText("Addison-Wesley")).check(matches(isDisplayed()))
        onView(withText("English")).check(matches(isDisplayed()))
        onView(withText("0134685997")).check(matches(isDisplayed()))
        onView(withText("9780134685991")).check(matches(isDisplayed()))
        onView(withText("416")).check(matches(isDisplayed()))
        onView(withText("2017")).check(matches(isDisplayed()))
        onView(withText("4/5")).check(matches(isDisplayed()))
        onView(withText("Java has changed dramatically since the previous edition of Effective Java was published shortly after the release of Java 6. This Jolt award-winning classic has now been thoroughly updated to take full advantage of the latest language and library features. The support in modern Java for multiple pa...")).check(matches(isDisplayed()))
        onView(withText("$38.00")).check(matches(isDisplayed()))
        onView(withId(R.id.big_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))

        onView(withId(R.id.memo_edit)).check(matches(isDisplayed()))
        onView(withId(R.id.small_image)).check(matches(isDisplayed()))
        onView(withId(R.id.bookmark_button)).check(matches(isNotChecked()))

        onView(withId(R.id.memo_edit)).perform(ViewActions.typeText("Effective Java"))

        onView(withId(R.id.memo_edit)).perform(ViewActions.pressImeActionButton())

        onView(withId(R.id.save_memo_button)).perform(click())

        runBlocking {
            repository.getBookByIsbn(1)?.memo?.let { Assert.assertEquals("Effective Java", it) }
        }

        mainActivityScenario.close()
    }

}