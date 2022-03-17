package com.example.mockito.repository

interface RepositoryContract {

    fun searchGithub(query: String, callback: RepositoryCallback)
}