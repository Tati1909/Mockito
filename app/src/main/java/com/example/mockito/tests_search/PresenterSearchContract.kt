package com.example.mockito.tests_search

import com.example.mockito.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract {

    fun searchGitHub(searchQuery: String)
}