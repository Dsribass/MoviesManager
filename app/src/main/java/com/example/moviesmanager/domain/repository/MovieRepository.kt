package com.example.moviesmanager.domain.repository

import com.example.moviesmanager.domain.model.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.UUID

interface MovieRepository {
    fun getMovieList(): Observable<List<Movie>>
//    fun getMovie(id: UUID): Single<Movie>
//    fun addMovie(movie: Movie): Completable
//    fun updateMovie(movie: Movie): Completable
//    fun removeMovie(id: UUID): Completable
}