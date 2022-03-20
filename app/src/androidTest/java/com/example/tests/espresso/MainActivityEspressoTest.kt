package com.example.tests.espresso

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.BuildConfig
import com.example.tests.R
import com.example.tests.tests_search.MainActivity
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            TestCase.assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        TestCase.assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView =
                it.findViewById<TextView>(R.id.totalCountTextViewMain)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    /**
     * Метод isDisplayed() вернет true если хотя бы часть View отображается на экране.
     */
    @Test
    fun activityTextView_IsDisplayed() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun activityButtons_AreEffectiveVisible() {
        onView(withId(R.id.searchEditText)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    /**
     * проверим, как у нас отрабатывает запрос на сервер:
     */
    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"))
        //нажать главную кнопку клавиатуры (в нашем случае - поиск)
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        onView(withId(R.id.searchEditText)).perform(closeSoftKeyboard())

        /**
         * BuildConfig.COUNT будет меняться в зависимости от типа сборки:
         * fakeDebug - COUNT = 42
         * realDebug - COUNT = 2937(значение меняется)
         */
        onView(isRoot()).perform(delay())
        onView(withId(R.id.totalCountTextViewMain))
            .check(matches(withText("Number of results: ${BuildConfig.COUNT}")))
    }

    /**
     * Нам нужно как-то поставить на паузу тест и дождаться ответа от сервера. Для этого
     * мы будем ставить на ожидание View, который мы планируем тестировать. Напишем вспомогательный метод.
     */
    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}