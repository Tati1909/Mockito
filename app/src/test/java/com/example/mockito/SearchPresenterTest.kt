package com.example.mockito

import com.example.mockito.model.SearchResponse
import com.example.mockito.model.SearchResult
import com.example.mockito.repository.GitHubRepository
import com.example.mockito.tests_search.SearchPresenter
import com.example.mockito.tests_search.ViewSearchContract
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

/**
 * Мы объявили три переменных: Презентер и два мока. Моков нам более чем
достаточно, потому что мы тестируем именно Презентер и ничего кроме заглушек для других
классов нам не понадобится. Таким образом мы избавимся от зависимостей и лишней
работы по инициализации, а также сможем протестировать Презентер в изоляции, не
задействуя инстансы других классов.
 */
class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
//Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        presenter = SearchPresenter(viewContract, repository)
    }

    @Test //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        val searchQuery = "some query"
        //Запускаем код, функционал которого хотим протестировать
        presenter.searchGitHub("some query")
        //Убеждаемся, что все работает как надо
        Mockito.verify(repository, Mockito.times(1)).searchGithub(searchQuery, presenter)
    }

    @Test //Проверяем работу метода handleGitHubError()
    fun handleGitHubError_Test() {
        //Вызываем у Презентера метод handleGitHubError()
        presenter.handleGitHubError()
        //Проверяем, что у viewContract вызывается метод displayError()
        Mockito.verify(viewContract, Mockito.times(1)).displayError()
    }

    //Проверяем работу метода handleGitHubResponse

    @Test //Для начала проверим, как приходит ответ сервера
    fun handleGitHubResponse_ResponseUnsuccessful() {
        //Создаем мок ответа сервера с типом Response<SearchResponse?>?
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        Mockito.`when`(response.isSuccessful).thenReturn(false)
        //Убеждаемся, что ответ действительно false
        Assert.assertFalse(response.isSuccessful)
    }

    @Test //Теперь проверим, как у нас обрабатываются ошибки
    fun handleGitHubResponse_Failure() {
        //Создаем мок ответа сервера с типом Response<SearchResponse?>?
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        //В таком случае должен вызываться метод viewContract.displayError(...)
        Mockito.`when`(response.isSuccessful).thenReturn(false)

        //Вызывваем у Презентера метод handleGitHubResponse()
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что вызывается верный метод: viewContract.displayError("Response is null or unsuccessful"), и что он вызывается единожды
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError("Response is null or unsuccessful")
    }

    @Test //Проверим порядок вызова методов viewContract
    fun handleGitHubResponse_ResponseFailure_ViewContractMethodOrder() {
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        Mockito.`when`(response.isSuccessful).thenReturn(false)
        presenter.handleGitHubResponse(response)

        //Определяем порядок вызова методов какого класса мы хотим проверить
        val inOrder = Mockito.inOrder(viewContract)
        //Прописываем порядок вызова методов
        inOrder.verify(viewContract).displayLoading(false)
        inOrder.verify(viewContract).displayError("Response is null or unsuccessful")
    }

    @Test //Проверим пустой ответ сервера
    fun handleGitHubResponse_ResponseIsEmpty() {
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        Mockito.`when`(response.body()).thenReturn(null)
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        Assert.assertNull(response.body())
    }

    @Test //Теперь проверим непустой ответ сервера
    fun handleGitHubResponse_ResponseIsNotEmpty() {
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        Mockito.`when`(response.body()).thenReturn(Mockito.mock(SearchResponse::class.java))
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        Assert.assertNotNull(response.body())
    }

    @Test //Проверим как обрабатывается случай, если ответ от сервера пришел пустой
    fun handleGitHubResponse_EmptyResponse() {
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        //Устанавливаем правило, что ответ успешный
        Mockito.`when`(response.isSuccessful).thenReturn(true)
        //При этом body ответа == null. В таком случае должен вызываться метод viewContract.displayError(...)
        Mockito.`when`(response.body()).thenReturn(null)

        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что вызывается верный метод: viewContract.displayError("Search results or total count are null"), и что он вызывается единожды
        Mockito.verify(viewContract, Mockito.times(1))
            .displayError("Search results or total count are null")
    }

    @Test //Пришло время проверить успешный ответ, так как все остальные случаи мы уже покрыли тестами
    fun handleGitHubResponse_Success() {
        //Мокаем ответ
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        //Мокаем тело ответа
        val searchResponse = Mockito.mock(SearchResponse::class.java)
        //Мокаем список
        val searchResults = listOf(Mockito.mock(SearchResult::class.java))

        //Прописываем правила
        //Когда ответ приходит, возвращаем response.isSuccessful == true и остальные данные в процессе выполнения метода handleGitHubResponse
        Mockito.`when`(response.isSuccessful).thenReturn(true)
        Mockito.`when`(response.body()).thenReturn(searchResponse)
        Mockito.`when`(searchResponse.searchResults).thenReturn(searchResults)
        Mockito.`when`(searchResponse.totalCount).thenReturn(101)

        //Запускаем выполнение метода
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что ответ от сервера обрабатывается корректно
        Mockito.verify(viewContract, Mockito.times(1)).displaySearchResults(searchResults, 101)
    }

    @After
    fun closeService() {
        closeable.close()
    }
}