package com.example.moviesmanager.presentation.movies.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesmanager.R
import com.example.moviesmanager.model.Movie
import java.util.*

class MovieListAdapter(
    private val movies: List<Movie>,
    private val onClick: (id: UUID) -> Unit,
    private val onEdit: (id: UUID) -> Unit,
    private val onDelete: (id: UUID) -> Unit,
) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = movies[position].name
        viewHolder.itemView.setOnClickListener { onClick(movies[position].id) }

        viewHolder.itemView.setOnLongClickListener {
            createOptionMenu(viewHolder, position)
            true
        }
    }

    private fun createOptionMenu(viewHolder: ViewHolder, position: Int) {
        val popupMenu = PopupMenu(viewHolder.itemView.context, viewHolder.itemView)
        popupMenu.inflate(R.menu.movie_options)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit_item -> {
                    onEdit(movies[position].id)
                    true
                }
                R.id.menu_delete_item -> {
                    onDelete(movies[position].id)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun getItemCount() = movies.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView

        init {
            title = view.findViewById(R.id.movie_item_title_text)
        }
    }
}
