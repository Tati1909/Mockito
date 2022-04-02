package com.example.tests.repository

import com.example.tests.tests_search.model.SearchResponse
import io.reactivex.Observable

interface RepositoryContract {

    fun searchGithub(query: String, callback: RepositoryCallback)

    /**
     * метод для запроса через rx, который будет возвращать Observable.
     */
    fun searchGithub(query: String): Observable<SearchResponse>
}