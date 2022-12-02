package com.example.moviesmanager.presentation.movies.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesmanager.R
import com.example.moviesmanager.model.Movie
import java.util.*

class MovieListAdapter(private val movies: List<Movie>, private val onClick: (id: UUID) -> Unit) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = movies[position].name
        viewHolder.itemView.setOnClickListener { onClick(movies[position].id) }
    }

    override fun getItemCount() = movies.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView

        init {
            title = view.findViewById(R.id.movie_item_title_text)
        }
    }
}
