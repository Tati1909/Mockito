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
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Для простоты примера мы будем инициализировать аргументы конструктора сразу
 */
class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubService::class.java)
    ),
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : ViewModel() {

    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData

    /**
     * определим свой CoroutineScope и будем обрабатывать исключения отдельно
     */
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
            + SupervisorJob()
            + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    /**
     * метод subscribeToLiveData возвращает liveData в виде ScreenState
     */
    fun subscribeToLiveData() = liveData

    /**
     * Запрашиваем данные у репозитория.
     */
    fun searchGitHub(searchQuery: String) {
        _liveData.value = ScreenState.Loading
        viewModelCoroutineScope.launch {
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.value = ScreenState.Working(searchResponse)
            } else {
                _liveData.value =
                    ScreenState.Error(Throwable("Search results or total count are null"))
            }
        }
    }

    private fun handleError(error: Throwable) {
        _liveData.value =
            ScreenState.Error(
                Throwable(
                    error.message ?: "Response is null or unsuccessful"
                )
            )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}