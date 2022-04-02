package com.example.tests.repository

import com.example.tests.tests_search.model.SearchResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Документация https://developer.github.com/v3/search/
 */

interface GitHubService {

    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/repositories")
    fun searchGithub(@Query("q") term: String?): Call<SearchResponse?>?

    /**
     * Запрос с пом RxJava
     */
    @Headers("Accept: application/vnd.github.mercy-preview+json")
    @GET("search/repositories")
    fun searchGithubRx(@Query("q") term: String?): Observable<SearchResponse>
}