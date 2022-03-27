package com.example.tests.espresso

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.R
import com.example.tests.TEST_NUMBER_OF_RESULTS_PLUS_1
import com.example.tests.tests_details.DetailsFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentEspressoTest {

    private lateinit var scenario: FragmentScenario<DetailsFragment>

    /**
     * Обратите внимание, как создается Фрагмент. Есть два основных метода для создания:
    ● launchFragmentInContainer() нужен для запуска Фрагмента с UI;
    ● launchFragment — для Фрагментов без UI.
     */
    @Before
    fun setup() {
//Запускаем Fragment в корне Activity
        scenario = launchFragmentInContainer()
    }

    /**
     * Проверим, как во Фрагмент передается бандл и отображается информация
     */
    @Test
    fun fragment_testBundle() {
//Можно передавать аргументы во Фрагмент, но это необязательно
        val fragmentArgs = bundleOf("TOTAL_COUNT_EXTRA" to 10)
//Запускаем Fragment с аргументами
        val scenario = launchFragmentInContainer<DetailsFragment>(fragmentArgs)
//Возможность менять стейт Фрагмента
        scenario.moveToState(Lifecycle.State.RESUMED)

        val assertion = matches(withText("Number of results: 10"))
        onView(withId(R.id.totalCountTextViewDetails)).check(assertion)
    }

    /**
     * проверяем работу метода setCount
     */
    @Test
    fun fragment_testSetCountMethod() {
        scenario.onFragment { fragment ->
            fragment.setCount(10)
        }
        val assertion = matches(withText("Number of results: 10"))
        onView(withId(R.id.totalCountTextViewDetails)).check(assertion)
    }

    /**
     * проверяем функциональность кнопок с помощью метода
    библиотеки Espresso onView(), знакомого нам по тестам Активити
     */
    @Test
    fun fragment_testIncrementButton() {
        onView(withId(R.id.incrementButton)).perform(click())
        onView(withId(R.id.totalCountTextViewDetails)).check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
    }
}