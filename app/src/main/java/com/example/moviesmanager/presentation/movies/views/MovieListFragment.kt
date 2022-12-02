package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentMovieListBinding
import com.example.moviesmanager.model.Movie
import com.example.moviesmanager.presentation.movies.views.adapter.MovieListAdapter
import com.example.moviesmanager.utils.Factory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        repository = Factory.makeMovieRepository(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables
            .add(
                repository
                    .getMovieList()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe { showMovieList(it) }
            )
    }

    private fun showMovieList(movies: List<Movie>) {
        binding.movieListView.adapter = MovieListAdapter(movies = movies)
        binding.movieListView.layoutManager = LinearLayoutManager(binding.root.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}