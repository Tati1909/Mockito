package com.example.tests.repository

interface RepositoryContract {

    fun searchGithub(query: String, callback: RepositoryCallback)
}