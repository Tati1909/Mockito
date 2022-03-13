package com.example.mockito.tests_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mockito.R
import com.example.mockito.model.SearchResult
import kotlinx.android.synthetic.main.list_item.view.repositoryName

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

        fun bind(searchResult: SearchResult) {
            itemView.repositoryName.text = searchResult.fullName
        }
    }
}