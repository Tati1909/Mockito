package com.example.tests.tests_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tests.R
import com.example.tests.databinding.ListItemBinding
import com.example.tests.model.SearchResult

class SearchResultAdapter(
    var results: List<SearchResult>
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchResultViewHolder {
        return SearchResultViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, null)
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = results[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ListItemBinding.bind(itemView)

        fun bind(searchResult: SearchResult) {
            binding.repositoryName.text = searchResult.fullName
            binding.repositoryName.setOnClickListener {
                Toast.makeText(itemView.context, searchResult.fullName,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}