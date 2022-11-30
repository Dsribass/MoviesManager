package com.example.moviesmanager.data.repository

import com.example.moviesmanager.domain.model.Movie
import com.example.moviesmanager.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Observable

class MovieRepositoryImpl : MovieRepository {
    override fun getMovieList(): Observable<List<Movie>> {
        TODO("Not yet implemented")
    }
}