package com.example.tests.espresso

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.BuildConfig
import com.example.tests.R
import com.example.tests.tests_search.MainActivity
import com.example.tests.tests_search.SearchResultAdapter
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * класс для тестирования списка
 */
@RunWith(AndroidJUnit4::class)
class MainActivityRecyclerViewTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    /**
     * метод, который будет прокручивать список до нужного элемента
     * Мы вводим запрос и отправляем его в Репозиторий. Неважно что именно мы вводим в качестве запроса —
     * Репозиторий всегда будет нам возвращать сгенерированный список из сотни позиций. Далее, когда запрос отправлен
     * и список загружен, мы находим наш RecyclerView по id и вызываем у него Action scrollTo, который стал нам доступен
    благодаря новой зависимости RecyclerViewActions. Метод hasDescendant ищет вью с надписью
    “FullName: 42”, то есть 42 элемент списка. И если находит, то проматывает список до этого элемента.
     */
    @Test
    fun activitySearch_ScrollTo() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 42"))
                ))
        }
    }

    /**
     * метод, который нажимает на элемент списка
     * Тут мы вызываем метод actionOnItemAtPosition, который в качестве аргументов принимает
    позицию элемента, с которым мы хотим взаимодействовать, и Action.
     */
    @Test
    fun activitySearch_PerformClickAtPosition() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()
            onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<SearchResultAdapter.SearchResultViewHolder>(
                        0,
                        click()
                    )
                )
        }
    }

    /**
     * будем нажимать на элемент списка, который не виден на экране.
    Для этого мы объединим функционал предыдущих методов: прокрутим список до нужного элемента и
    нажмем на него
     */
    @Test
    fun activitySearch_PerformClickOnItem() {
        if (BuildConfig.TYPE == MainActivity.FAKE) {
            loadList()

            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.scrollTo<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 50"))
                    )
                )
            onView(withId(R.id.recyclerView))
                .perform(
                    RecyclerViewActions.actionOnItem<SearchResultAdapter.SearchResultViewHolder>(
                        hasDescendant(withText("FullName: 42")),
                        click()
                    )
                )
        }
    }

    //вынестим общий код в отдельный метод
    private fun loadList() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
    }

    /**
     * Использовать методы типа hasDescendant для поиска на экране нужных view не очень красиво
    и довольно неоптимально. Для таких вещей лучше переопределить класс ViewAction, который
    принимают методы взаимодействия с UI из библиотеки Espresso. Это сильно облегчает работу с
    ViewHolder’ом списка, позволяя не только получить view по id, но и произвести над ним любые
    действия.
    Добавим новый метод, который будет принимать id требуемого view и возвращать нам готовый
    ViewAction, который умеет нажимать на этот view.
     */
    private fun tapOnItemWithId(id: Int) = object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Нажимаем на view с указанным id"
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById(id) as View
            v.performClick()
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}