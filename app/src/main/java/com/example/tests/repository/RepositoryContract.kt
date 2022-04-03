package com.example.tests.repository

import com.example.tests.tests_search.model.SearchResponse

interface RepositoryContract {

    suspend fun searchGithubAsync(query: String): SearchResponse
}