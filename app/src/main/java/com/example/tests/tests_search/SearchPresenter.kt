package com.example.tests.tests_search

import android.util.Log
import com.example.tests.provider.SchedulerProvider
import com.example.tests.provider.SearchSchedulerProvider
import com.example.tests.repository.RepositoryCallback
import com.example.tests.repository.RepositoryContract
import com.example.tests.tests_search.model.SearchResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter internal constructor(
    private val viewContract: ViewSearchContract,
    private val repository: RepositoryContract,
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : PresenterSearchContract, RepositoryCallback {

    /**
     * Запрос мы помещаем в CompositeDisposable, который должен очищаться в методе onDetach реального Презентера
     * (не забывайте об этом). Далее запрашиваем данные у репозитория, выполняем их в потоке IO (который нам возвращает
     * наш SearchSchedulerProvider). Ответ обрабатываем в главном потоке приложения. Далее используем два
    оператора, которые позволяют нам совершить какие-то действия непосредственно перед отправкой
    запроса на сервер и сразу после того, как запрос исполнился (или завершился с ошибкой). В
    doOnSubscribe мы отображаем загрузку, в doOnTerminate загрузку прячем. В оператор subscribeWith
    передаем лямбду и переопределяем три метода. В onNext обрабатываем полученные данные, в
    onError обрабатываем ошибку, onComplete не трогаем
     */
    override fun searchGitHub(searchQuery: String) {
//Dispose
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { viewContract.displayLoading(true) }
                .doOnTerminate { viewContract.displayLoading(false) }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            viewContract.displaySearchResults(
                                searchResults,
                                totalCount
                            )
                        } else {
                            viewContract.displayError("Search results or total count are null")
                        }
                    }

                    override fun onError(e: Throwable) {
                        viewContract.displayError(e.message ?: "Response is null or unsuccessful")
                    }

                    override fun onComplete() {}
                }
                )
        )
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                viewContract.displayError("Search results or total count are null")
            }
        } else {
            viewContract.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleGitHubError() {
        viewContract.displayLoading(false)
        viewContract.displayError()
    }

    override fun onDetach() {
        Log.d("MainActivity", "onDetach")

        super.onDetach()
    }
}