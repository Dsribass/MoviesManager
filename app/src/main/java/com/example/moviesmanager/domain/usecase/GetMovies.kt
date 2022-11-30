package com.example.moviesmanager.domain.usecase

import com.example.moviesmanager.domain.model.Movie
import com.example.moviesmanager.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Observable

class GetMovies(private val repository: MovieRepository) {
    fun execute(): Observable<List<Movie>> {
        return repository.getMovieList()
    }
}