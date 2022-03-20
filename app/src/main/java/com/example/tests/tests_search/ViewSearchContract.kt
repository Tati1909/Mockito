package com.example.tests.tests_search

import com.example.tests.tests_search.model.SearchResult
import com.example.tests.view.ViewContract

internal interface ViewSearchContract : ViewContract {

    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}