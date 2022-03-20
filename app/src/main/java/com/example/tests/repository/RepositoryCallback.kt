package com.example.tests.repository

import com.example.tests.tests_search.model.SearchResponse
import retrofit2.Response

interface RepositoryCallback {

    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}