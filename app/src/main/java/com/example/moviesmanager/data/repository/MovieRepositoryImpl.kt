package com.example.moviesmanager.data.repository

import com.example.moviesmanager.data.dao.MovieDAO
import com.example.moviesmanager.data.entity.MovieEntity
import com.example.moviesmanager.model.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class MovieRepositoryImpl(private val dao: MovieDAO) : MovieRepository {
    override fun getMovieList(): Observable<List<Movie>> {
        return dao.getMovieList().map { movieList ->
            movieList.map { it.toModel() }
        }
    }

    override fun addMovie(movie: Movie): Completable {
        return dao.addMovie(MovieEntity.fromModel(movie))
    }
}