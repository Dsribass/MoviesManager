package com.example.moviesmanager.data.dao

import androidx.room.*
import com.example.moviesmanager.data.entity.MovieEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface MovieDao {
    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME}")
    fun getMovieList(): Observable<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movieEntity: MovieEntity): Completable
}