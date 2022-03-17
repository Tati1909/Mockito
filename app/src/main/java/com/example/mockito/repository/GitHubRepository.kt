package com.example.mockito.repository

import com.example.mockito.tests_search.model.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubRepository(private val gitHubService: GitHubService) : RepositoryContract {

    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        val call = gitHubService.searchGithub(query)
        call?.enqueue(object : Callback<SearchResponse?> {

            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                callback.handleGitHubResponse(response)
            }

            override fun onFailure(
                call: Call<SearchResponse?>,
                t: Throwable
            ) {
                callback.handleGitHubError()
            }
        })
    }
}