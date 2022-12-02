package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentMovieDetailBinding
import com.example.moviesmanager.model.Movie
import com.example.moviesmanager.utils.Factory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.SingleSubject
import java.util.*

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val id: UUID by lazy { UUID.fromString(this.arguments?.get("id").toString()) }

    private lateinit var repository: MovieRepository
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        repository = Factory.makeMovieRepository(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository.getMovie(id)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { movie, _ -> showMovieDetails(movie) }
    }

    private fun showMovieDetails(movie: Movie) {
        movie.run {
            binding.movieDetailTitleText.text = name
            binding.movieDetailReleaseDateText.text = releaseDate.toString()
            binding.movieDetailStudioText.text = studio
            binding.movieDetailDurationText.text = duration.toString()
            binding.movieDetailGenreText.text = movie.genre.name
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.clear()
    }
}