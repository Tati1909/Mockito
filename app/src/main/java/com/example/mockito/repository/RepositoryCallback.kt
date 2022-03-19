package com.example.mockito.repository

import com.example.mockito.tests_search.model.SearchResponse
import retrofit2.Response

interface RepositoryCallback {

    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}