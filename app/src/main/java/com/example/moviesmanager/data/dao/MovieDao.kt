package com.example.moviesmanager.data.dao

import androidx.room.*
import com.example.moviesmanager.data.entity.MovieEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {
    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME}")
    fun getMovieList(): Observable<List<MovieEntity>>

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE ${MovieEntity.ID} LIKE :id")
    fun getMovie(id: String): Single<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movieEntity: MovieEntity): Completable

    @Update
    fun updateMovie(movieEntity: MovieEntity): Completable

    @Delete
    fun deleteMovie(movieEntity: MovieEntity): Completable
}