package com.example.mockito

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mockito.view.details.DetailsActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsActivityTest {

    /**
     * Активити, для которой будем писать тесты.
     * Вспомогательный класс ActivityScenario. Класс предназначен для тестирования жизненного цикла Активити и
     * ее элементов, таких как кнопки, текст и т. п.
     */
    private lateinit var scenario: ActivityScenario<DetailsActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(DetailsActivity::class.java)
        context = ApplicationProvider.getApplicationContext()
    }

    /**
     * Тест позволит нам убедиться, что DetailsActivity действительно корректно создается.
     * Проверим, что она существует. Метод сценария onActivity() позволяет получить созданную Активити
     */
    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    /**
     * Убедимся, что Активити в нужном нам состоянии
     */
    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    /**
     * Проверим, что соответствующие элементы Активити (в нашем случае это один
    единственный TextView) существуют (то есть загружается нужный нам layout):
     */
    @Test
    fun activityTextView_NotNull() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertNotNull(totalCountTextView)
        }
    }

    /**
     * Убеждаемся, что текстовое поле отображает ожидаемую информацию и видно на экране.
     */
    @Test
    fun activityTextView_HasText() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals("Number of results: 0", totalCountTextView.text)
        }
    }

    @Test
    fun activityTextView_IsVisible() {
        scenario.onActivity {
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            assertEquals(View.VISIBLE, totalCountTextView.visibility)
        }
    }

    /**
     * Убедимся, что кнопки видны на экране
     */
    @Test
    fun activityButtons_AreVisible() {
        scenario.onActivity {
            val decrementButton = it.findViewById<Button>(R.id.decrementButton)
            assertEquals(View.VISIBLE, decrementButton.visibility)

            val incrementButton = it.findViewById<Button>(R.id.incrementButton)
            assertEquals(View.VISIBLE, incrementButton.visibility)
        }
    }

    /**
     * Проверим, как нажатие на кнопку изменяет значение в TextView.
     * Чтобы имитировать нажатие на кнопку, мы вызываем метод performClick():
     */
    @Test
    fun activityButtonIncrement_IsWorking() {
        scenario.onActivity {
            val incrementButton = it.findViewById<Button>(R.id.incrementButton)
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            incrementButton.performClick()

            assertEquals("Number of results: 1", totalCountTextView.text)
        }
    }

    @Test
    fun activityButtonDecrement_IsWorking() {
        scenario.onActivity {
            val decrementButton = it.findViewById<Button>(R.id.decrementButton)
            val totalCountTextView = it.findViewById<TextView>(R.id.totalCountTextView)
            decrementButton.performClick()

            assertEquals("Number of results: -1", totalCountTextView.text)
        }
    }

    /**
     * Проверяем intent
     */
    @Test
    fun activityCreateIntent_NotNull() {
        val intent = DetailsActivity.getIntent(context, 0)
        assertNotNull(intent)
    }

    @Test
    fun activityCreateIntent_HasExtras() {
        val intent = DetailsActivity.getIntent(context, 0)
        val bundle = intent.extras
        assertNotNull(bundle)
    }

    /**
     * Убедимся, что intent создается корректно и содержит правильные данные. Обратите внимание на то, как мы получаем
    Контекст для создания Интента — мы используем еще один вспомогательный класс
    ApplicationProvider:
     */
    @Test
    fun activityCreateIntent_HasCount() {
        val count = 42
        val intent = DetailsActivity.getIntent(context, count)
        val bundle = intent.extras
        assertEquals(count, bundle?.getInt(DetailsActivity.TOTAL_COUNT_EXTRA, 0))
    }

    /**
     * Мы “закрываем” сценарий, когда все тесты пройдены. Сценарий не уничтожает
    созданную Активити самостоятельно и не высвобождает ресурсы.
     */
    @After
    fun close() {
        scenario.close()
    }
}
