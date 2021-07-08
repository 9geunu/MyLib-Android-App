package com.gunu.mylib

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gunu.mylib.domain.Book
import com.gunu.mylib.domain.IRepository
import com.gunu.mylib.data.ServiceLocator
import com.gunu.mylib.ui.newbook.NewBookFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewViewModelTest {

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
    fun testNewFragment() {
        runBlocking {
            repository.insertBook(
                Book(
                id = 1,
                title = "title",
                subtitle = "subtitle",
                isbn13 = "isbn13",
                price = "price",
                image = "image",
                url = "url",
                isBookmarked = false
            ))
        }

        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val todoFrag = launchFragmentInContainer<NewBookFragment>(Bundle(), R.style.AppTheme)

        todoFrag.onFragment { fragment ->

            navController.setGraph(R.navigation.mobile_navigation)
            navController.setCurrentDestination(R.id.navigation_newbook)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withText("title")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("subtitle")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("isbn13")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText("price")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}