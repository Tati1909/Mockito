package com.example.tests.tests_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tests.provider.SchedulerProvider
import com.example.tests.provider.SearchSchedulerProvider
import com.example.tests.repository.GitHubRepository
import com.example.tests.repository.GitHubService
import com.example.tests.repository.RepositoryContract
import com.example.tests.tests_search.MainActivity.Companion.BASE_URL
import com.example.tests.tests_search.model.SearchResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Для простоты примера мы будем инициализировать аргументы конструктора сразу
 */
class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubService::class.java)
    ),
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : ViewModel() {

    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData

    /**
     * метод subscribeToLiveData возвращает liveData в виде ScreenState
     */
    fun subscribeToLiveData() = liveData

    /**
     * Запрос мы помещаем в CompositeDisposable. Далее запрашиваем данные у репозитория, выполняем их в потоке IO
     * (который нам возвращает наш SearchSchedulerProvider). Ответ обрабатываем в главном потоке приложения.
     * Далее используем два оператора, которые позволяют нам совершить какие-то действия непосредственно перед отправкой
    запроса на сервер и сразу после того, как запрос исполнился (или завершился с ошибкой).
    В doOnSubscribe мы отображаем загрузку. В оператор subscribeWith передаем лямбду и переопределяем три метода.
    В onNext обрабатываем полученные данные, в onError обрабатываем ошибку, onComplete не трогаем
     */
    fun searchGitHub(searchQuery: String) {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { _liveData.value = ScreenState.Loading }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            _liveData.value = ScreenState.Working(searchResponse)
                        } else {
                            _liveData.value =
                                ScreenState.Error(Throwable("Search results or total count are null"))
                        }
                    }

                    override fun onError(e: Throwable) {
                        _liveData.value = ScreenState.Error(
                            Throwable(e.message ?: "Response is null or unsuccessful")
                        )
                    }

                    override fun onComplete() {}
                }
                )
        )
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}