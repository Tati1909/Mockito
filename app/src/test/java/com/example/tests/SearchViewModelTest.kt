package com.example.tests

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tests.presenter.ScheduleProviderStub
import com.example.tests.repository.FakeGitHubRepository
import com.example.tests.tests_search.ScreenState
import com.example.tests.tests_search.SearchViewModel
import com.example.tests.tests_search.model.SearchResponse
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@ExperimentalCoroutinesApi
class SearchViewModelTest {

    /**
     * InstantTaskExecutorRule нужен для тестирования LiveData
     */
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    /**
     * InstantTaskExecutorRule нужен для тестирования Coroutines
     */
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: FakeGitHubRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchViewModel = SearchViewModel(repository, ScheduleProviderStub())
    }

    /**
     * Убедимся, что LiveData действительно вызывается и возвращает какой-то объект.
     */
    @Test
    fun liveData_TestReturnValueIsNotNull() {
        //тестовый код выполняется в рамках Правила, которое умеет работать с suspend-функциями:
        testCoroutineRule.runBlockingTest {
            //Создаем обсервер. В лямбде мы не вызываем никакие методы - в этом нет необходимости
            //так как мы проверяем работу LiveData и не собираемся ничего делать с данными, которые она возвращает
            val observer = Observer<ScreenState> {}
            //Получаем LiveData
            val liveData = searchViewModel.subscribeToLiveData()

            //При вызове Репозитория возвращаем шаблонные данные
            Mockito.`when`(repository.searchGithub(SEARCH_QUERY)).thenReturn(
                Observable.just(
                    SearchResponse(
                        1,
                        listOf()
                    )
                )
            )

            try {
                //Подписываемся на LiveData без учета жизненного цикла
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)
                //Убеждаемся, что Репозиторий вернул данные и LiveData передала их Наблюдателям
                Assert.assertNotNull(liveData.value)
            } finally {
                //Тест закончен, снимаем Наблюдателя
                liveData.removeObserver(observer)
            }
        }
    }

    /**
     * Проверим, что за объекты она может возвращать и насколько они соответствуют нашим ожиданиям.
     */
    @Test
    fun coroutines_TestReturnValueIsError() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            `when`(repository.searchGithubAsync(SEARCH_QUERY)).thenReturn(
                SearchResponse(null, listOf())
            )
            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, ERROR_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    /**
     * проверим, как у нас обрабатываются выбрасываемые исключения, без вызова методов Репозитория. То есть метод
    SearchViewModel.searchGitHub(query) вызовется, а метод repository.searchGithubAsync(query) — нет.
    Это приведет к некорректной работе Корутин и наш CoroutineExceptionHandler выбросит исключение,
    вызвав метод handleError(throwable) и, соответственно, _liveData.value = ScreenState.Error(...):
     */
    @Test
    fun coroutines_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<ScreenState> {}
            val liveData = searchViewModel.subscribeToLiveData()

            try {
                liveData.observeForever(observer)
                searchViewModel.searchGitHub(SEARCH_QUERY)

                val value: ScreenState.Error = liveData.value as ScreenState.Error
                Assert.assertEquals(value.error.message, EXCEPTION_TEXT)
            } finally {
                liveData.removeObserver(observer)
            }
        }
    }

    companion object {

        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "Search results or total count are null"
        private const val EXCEPTION_TEXT = "Response is null or unsuccessful"
    }
}