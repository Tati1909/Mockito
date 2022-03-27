package com.example.tests.repository

import com.example.tests.tests_search.model.SearchResponse
import com.example.tests.tests_search.model.SearchResult
import retrofit2.Response
import kotlin.random.Random

/**
 * тестовый Репозиторий, который наследует наш интерфейс и по запросу сразу
возвращает тестовые данные:
 */
class FakeGitHubRepository : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        callback.handleGitHubResponse(Response.success(getFakeResponse()))
    }

    /**
     * будем генерировать данные для нашего списка, чтобы было что проверять,
     * ведь сейчас Репозиторий возвращает пустой список
     */
    private fun getFakeResponse(): SearchResponse {
        val list: MutableList<SearchResult> = mutableListOf()
        for (index in 1..100) {
            list.add(
                SearchResult(
                    id = index,
                    name = "Name: $index",
                    fullName = "FullName: $index",
                    private = Random.nextBoolean(),
                    description = "Description: $index",
                    updatedAt = "Updated: $index",
                    size = index,
                    stargazersCount = Random.nextInt(100),
                    language = "",
                    hasWiki = Random.nextBoolean(),
                    archived = Random.nextBoolean(),
                    score = index.toDouble()
                )
            )
        }
        return SearchResponse(list.size, list)
    }
}