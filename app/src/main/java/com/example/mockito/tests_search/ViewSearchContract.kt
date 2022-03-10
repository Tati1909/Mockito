package com.example.mockito.tests_search

import com.example.mockito.model.SearchResult
import com.example.mockito.view.ViewContract

internal interface ViewSearchContract : ViewContract {

    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}