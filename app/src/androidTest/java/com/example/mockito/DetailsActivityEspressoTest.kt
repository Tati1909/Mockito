package com.example.mockito

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.mockito.tests_details.DetailsActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailsActivityEspressoTest {

    /**
     * создаем сценарий и через сценарий убеждаемся, что все базовые вещи работают.
     */
    private lateinit var scenario: ActivityScenario<DetailsActivity>

    /**
     *  ActivityScenario -  класс тестового фреймворка, умеет запускать нужные вам Активити без доступа к Контексту,
     *  так как разработчик в принципе не может самостоятельно запустить Активити, а только создает Интент и перенаправляет
    его ОС. Помимо этого ActivityScenario предоставляет вам возможность управлять жизненным циклом
    Активити, что помогает во многих сценариях тестирования.
     */
    @Before
    fun setup() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
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
                it.findViewById<TextView>(R.id.totalCountTextView)
            TestCase.assertNotNull(totalCountTextView)
        }
    }

    /**
     * Убедимся, что TextView отображает текст, который положено отображать:
     */
    @Test
    fun activityTextView_HasText() {
        val assertion: ViewAssertion = matches(withText("Number of results: 0"))
        onView(withId(R.id.totalCountTextView)).check(assertion)
    }

    @After
    fun close() {
        scenario.close()
    }
}