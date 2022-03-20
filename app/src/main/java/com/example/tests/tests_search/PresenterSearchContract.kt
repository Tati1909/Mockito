package com.example.tests.tests_search

import com.example.tests.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract {

    fun searchGitHub(searchQuery: String)
}