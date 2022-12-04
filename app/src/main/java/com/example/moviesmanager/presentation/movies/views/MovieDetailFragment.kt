package com.example.moviesmanager.presentation.movies.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.moviesmanager.R
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentMovieDetailBinding
import com.example.moviesmanager.databinding.RateMovieDialogContentBinding
import com.example.moviesmanager.model.Movie
import com.example.moviesmanager.utils.Factory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val id: UUID by lazy { UUID.fromString(this.arguments?.get("id").toString()) }

    private lateinit var repository: MovieRepository
    private val disposables = CompositeDisposable()
    private val onConfirmMovieRateSubject = PublishSubject.create<Int>()
    private val onRefreshSubject = PublishSubject.create<Unit>()

    private var movieDetail: Movie? = null

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
        setupObservables()
    }

    private fun setupObservables() {
        Observable.merge(
            listOf(
                Observable.just(Unit),
                onRefreshSubject
            )
        )
            .flatMapSingle { repository.getMovie(id) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    movieDetail = it
                    showMovieDetails(it)
                },
                {
                    Toast.makeText(
                        binding.root.context,
                        it.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                },
            )
            .addTo(disposables)

        binding.rateMovieButton.setOnClickListener {
            createRateMovieDialog {
                if (it.text.isEmpty()) {
                    it.setText("0")
                }
                onConfirmMovieRateSubject.onNext("${it.text}".toInt())
            }
        }

        onConfirmMovieRateSubject
            .subscribe {
                if (movieDetail != null) {
                    repository.updateMovie(
                        Movie(
                            id = movieDetail!!.id,
                            name = movieDetail!!.name,
                            studio = movieDetail!!.studio,
                            duration = movieDetail!!.duration,
                            releaseDate = movieDetail!!.releaseDate,
                            genre = movieDetail!!.genre,
                            rate = it,
                        )
                    ).subscribe {
                        onRefreshSubject.onNext(Unit)
                    }
                        .addTo(disposables)
                }
            }
            .addTo(disposables)
    }

    private fun showMovieDetails(movie: Movie) {
        movie.run {
            binding.movieDetailTitleText.text = name
            binding.movieDetailReleaseDateText.text = releaseDate.toString()
            binding.movieDetailStudioText.text = studio
            binding.movieDetailDurationText.text = duration.toString()
            binding.movieDetailGenreText.text = movie.genre.name
            if (wasWatched) {
                binding.movieDetailRateContent.visibility = View.VISIBLE
                binding.movieDetailRate.text = "$rate/10"
                binding.rateMovieButton.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.clear()
    }

    private fun createRateMovieDialog(onConfirmButton: (EditText) -> Unit) {
        val builder = AlertDialog.Builder(context)
        val content = RateMovieDialogContentBinding.inflate(layoutInflater, null, false)
        builder.setMessage(R.string.rate)
            .setView(content.root)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ ->
                onConfirmButton(content.rateContentEditText)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
        builder.show()
    }
}