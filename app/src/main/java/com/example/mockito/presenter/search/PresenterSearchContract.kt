package com.example.mockito.presenter.search

import com.example.mockito.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract {

    fun searchGitHub(searchQuery: String)
}