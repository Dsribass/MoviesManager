package com.example.moviesmanager.data.repository

import com.example.moviesmanager.data.dao.MovieDao
import com.example.moviesmanager.data.entity.MovieEntity
import com.example.moviesmanager.model.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

class MovieRepositoryImpl(private val dao: MovieDao) : MovieRepository {
    override fun getMovieList(): Observable<List<Movie>> {
        return dao.getMovieList().map { movieList ->
            movieList.map { it.toModel() }
        }
    }

    override fun getMovie(id: UUID): Single<Movie> {
        return dao.getMovie(id.toString()).map { it.toModel() }
    }

    override fun addMovie(movie: Movie): Completable {
        return dao.addMovie(MovieEntity.fromModel(movie))
    }

    override fun updateMovie(movie: Movie): Completable {
        return dao.updateMovie(MovieEntity.fromModel(movie))
    }

    override fun removeMovie(id: UUID): Completable {
        return getMovie(id).flatMapCompletable { dao.deleteMovie(MovieEntity.fromModel(it)) }
    }
}