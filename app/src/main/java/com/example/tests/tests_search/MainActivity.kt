package com.example.tests.tests_search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tests.R
import com.example.tests.databinding.ActivityMainBinding
import com.example.tests.tests_details.DetailsActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val adapterUsers by lazy {
        SearchResultAdapter(results = ArrayList())
    }
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
        viewModel.subscribeToLiveData().observe(this) { onStateChange(it) }
    }

    private fun onStateChange(screenState: ScreenState) {
        when (screenState) {
            is ScreenState.Working -> {
                val searchResponse = screenState.searchResponse
                val totalCount = searchResponse.totalCount
                binding.progressBar.visibility = View.GONE
                with(binding.totalCountTextViewMain) {
                    visibility = View.VISIBLE
                    text =
                        String.format(
                            Locale.getDefault(),
                            getString(R.string.results_count),
                            totalCount
                        )
                }
                this.totalCount = totalCount!!
                adapterUsers.results = searchResponse.searchResults!!
                adapterUsers.notifyDataSetChanged()
            }
            is ScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ScreenState.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, screenState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * теперь по кнопке TO DETAILS у нас будет открываться экран с количеством найденных репозиториев.
     * totalCount - количество найденных репозиториев
     */
    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapterUsers
    }

    /**
     * кнопку поиска будем кликать на клавиатуре
     */
    private fun setQueryListener() {
        binding.searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotBlank()) {
                    viewModel.searchGitHub(query)
                    return@OnEditorActionListener true
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.enter_search_word),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener false
                }
            }
            false
        })
    }

    companion object {

        const val FAKE = "FAKE"
        const val BASE_URL = "https://api.github.com"
    }
}