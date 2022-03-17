package com.example.mockito.repository

import com.example.mockito.tests_search.model.SearchResponse
import retrofit2.Response

/**
 * тестовый Репозиторий, который наследует наш интерфейс и по запросу сразу
возвращает тестовые данные:
 */
class FakeGitHubRepository : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        callback.handleGitHubResponse(Response.success(SearchResponse(42, listOf())))
    }
}