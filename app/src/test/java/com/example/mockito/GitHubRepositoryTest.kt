package com.example.mockito

import com.example.mockito.model.SearchResponse
import com.example.mockito.repository.GitHubApi
import com.example.mockito.repository.GitHubRepository
import okhttp3.Request
import okio.Timeout
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubRepositoryTest {

    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var gitHubApi: GitHubApi

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = GitHubRepository(gitHubApi)
    }

    @Test
    fun searchGithub_Test() {
        val searchQuery = "some query"
        val call = Mockito.mock(Call::class.java) as Call<SearchResponse?>

        Mockito.`when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(searchQuery, Mockito.mock(GitHubRepository.GitHubRepositoryCallback::class.java))
        Mockito.verify(gitHubApi, Mockito.times(1)).searchGithub(searchQuery)
    }

    @Test
    fun searchGithub_TestCallback() {
        val searchQuery = "some query"
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>
        val gitHubRepositoryCallBack = Mockito.mock(GitHubRepository.GitHubRepositoryCallback::class.java)

        val call = object : Call<SearchResponse?> {
            override fun enqueue(callback: Callback<SearchResponse?>) {
                callback.onResponse(this, response)
                callback.onFailure(this, Throwable())
            }

            override fun clone(): Call<SearchResponse?> {
                TODO("Not yet implemented")
            }

            override fun execute(): Response<SearchResponse?> {
                TODO("Not yet implemented")
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                TODO("Not yet implemented")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }

        Mockito.`when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        repository.searchGithub(searchQuery, gitHubRepositoryCallBack)

        Mockito.verify(gitHubRepositoryCallBack, Mockito.times(1)).handleGitHubResponse(response)
        Mockito.verify(gitHubRepositoryCallBack, Mockito.times(1)).handleGitHubError()
    }

    @Test
    fun searchGithub_TestCallback_WithMock() {
        val searchQuery = "some query"
        val call = Mockito.mock(Call::class.java) as Call<SearchResponse?>
        val callBack = Mockito.mock(Callback::class.java) as Callback<SearchResponse?>
        val gitHubRepositoryCallBack = Mockito.mock(GitHubRepository.GitHubRepositoryCallback::class.java)
        val response = Mockito.mock(Response::class.java) as Response<SearchResponse?>

        Mockito.`when`(gitHubApi.searchGithub(searchQuery)).thenReturn(call)
        Mockito.`when`(call.enqueue(callBack)).then {
            callBack.onResponse(Mockito.any(), Mockito.any())
        }
        Mockito.`when`(callBack.onResponse(Mockito.any(), Mockito.any())).then {
            gitHubRepositoryCallBack.handleGitHubResponse(response)
        }

        repository.searchGithub(searchQuery, gitHubRepositoryCallBack)

        Mockito.verify(gitHubRepositoryCallBack, Mockito.times(1)).handleGitHubResponse(response)
    }
}