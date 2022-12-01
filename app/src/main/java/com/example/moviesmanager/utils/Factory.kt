package com.example.moviesmanager.utils

import android.content.Context
import com.example.moviesmanager.data.database.ApplicationDatabase
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.data.repository.MovieRepositoryImpl

class Factory {
    companion object {
        fun makeMovieRepository(context: Context): MovieRepository {
            val db = ApplicationDatabase.getDatabaseInstance(context)
            return MovieRepositoryImpl(db.movieDao())
        }
    }
}