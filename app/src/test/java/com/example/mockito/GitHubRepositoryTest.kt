package com.example.mockito

import com.example.mockito.repository.GitHubRepository
import com.example.mockito.repository.GitHubService
import com.example.mockito.tests_search.model.SearchResponse
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call

class GitHubRepositoryTest {

    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var gitHubService: GitHubService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = GitHubRepository(gitHubService)
    }

    @Test

    fun searchRepos() {
        val searchQuery = "some query"
        val call = mock(Call::class.java) as Call<SearchResponse?>
        `when`(gitHubService.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(
            searchQuery,
            mock(GitHubRepository.GitHubRepositoryCallback::class.java)
        )
        verify(gitHubService, times(1)).searchGithub(searchQuery)
    }
}