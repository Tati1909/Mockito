package com.example.tests.automator

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import com.example.tests.R
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * На эмуляторе данные грузятся дольше, так что некоторые тесты могут не проходить.
 * Лучше пользоваться реальной мобилкой.
 */
@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 23)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        //Мы уже проверяли Интент на null в предыдущем тесте, поэтому допускаем, что Интент у нас не null
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)//Чистим бэкстек от запущенных ранее Активити
        context.startActivity(intent)

        //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Проверяем на null
        Assert.assertNotNull(editText)
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "automator"
        //Отправляем запрос через Espresso
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())

        //Ожидаем конкретного события: появления текстового поля totalCountTextViewMain.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextViewMain")),
                TIMEOUT
            )
        //Убеждаемся, что сервер вернул корректный результат. Не забываем, что количество
        //результатов может варьироваться во времени, потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), "Number of results: 5723")
    }

    //Убеждаемся, что DetailsScreen открывается
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextViewDetails")),
                TIMEOUT
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        Assert.assertEquals(changedText.text, "Number of results: 0")
    }

    //Убеждаемся, что если нажать на + в DetailsScreen, то в результате будет отображаться 1
    @Test
    fun test_CheckIncrementButton() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextViewDetails")),
                TIMEOUT
            )

        //Находим кнопку
        val incrementButton: UiObject2 = uiDevice.findObject(By.res(packageName, "incrementButton"))
        //Кликаем по ней
        incrementButton.click()
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        Assert.assertEquals(changedText.text, "Number of results: 1")
    }

    //Убеждаемся, что если нажать на - в DetailsScreen, то в результате будет отображаться -1
    @Test
    fun test_CheckDecrementButton() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextViewDetails")),
                TIMEOUT
            )

        //Находим кнопку
        val decrementButton: UiObject2 = uiDevice.findObject(By.res(packageName, "decrementButton"))
        //Кликаем по ней
        decrementButton.click()
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        Assert.assertEquals(changedText.text, "Number of results: -1")
    }

    //Убеждаемся, что DetailsScreen открывается и кнопка decrement notNull
    @Test
    fun test_OpenDetailsScreenAndVisibilityDecrementButton() {
        //Находим кнопку
        val toDetails: UiObject2 = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val buttonDecrement =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "decrementButton")),
                TIMEOUT
            )
        Assert.assertNotNull(buttonDecrement)
    }

    /** Убеждаемся, что когда вводим слово "automator", и переходим на DetailsScreen,
     * то экран действительно открывается и отображается верный результат в totalCountTextViewDetails
     */
    @Test
    fun test_OpenDetailsScreenAndCheckResult() {
        //Через uiDevice находим editText
        val editText = uiDevice.findObject(By.res(packageName, "searchEditText"))
        //Устанавливаем значение
        editText.text = "automator"
        //Отправляем запрос через Espresso
        Espresso.onView(ViewMatchers.withId(R.id.searchEditText))
            .perform(ViewActions.pressImeActionButton())
        //ждем 4 секунды
        Espresso.onView(ViewMatchers.isRoot()).perform(delay())

        //Находим кнопку toDetails
        val toDetails: UiObject2 = uiDevice.findObject(By.res(packageName, "toDetailsActivityButton"))
        //Кликаем по ней
        toDetails.click()
        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText =
            uiDevice.wait(
                Until.findObject(By.res(packageName, "totalCountTextViewDetails")),
                TIMEOUT
            )
        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        Assert.assertEquals(changedText.text, "Number of results: 5723")
    }

    /**
     * Нам нужно как-то поставить на паузу тест и дождаться ответа от сервера. Для этого
     * мы будем ставить на ожидание View, который мы планируем тестировать. Напишем вспомогательный метод.
     */
    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $4seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(4000)
            }
        }
    }

    companion object {

        private const val TIMEOUT = 5000L
    }
}