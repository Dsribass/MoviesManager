package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesmanager.R
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentMovieListBinding
import com.example.moviesmanager.model.Movie
import com.example.moviesmanager.presentation.movies.views.adapter.MovieListAdapter
import com.example.moviesmanager.utils.Factory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository
    private val disposables = CompositeDisposable()

    private val onMovieListSubject = BehaviorSubject.create<List<Movie>>()
    private val onMovieList: Observable<List<Movie>> = onMovieListSubject
    private val movieList = mutableListOf<Movie>()

    private val onTapItemSubject = PublishSubject.create<UUID>()

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
        setupMovieListView()
        setupObservables()
    }

    private fun setupObservables() {
        repository
            .getMovieList()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(onMovieListSubject)

        disposables.addAll(
            onMovieList
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        movieList.clear()
                        movieList.addAll(it)
                        binding.movieListView.adapter?.notifyDataSetChanged()
                    },
                    {
                        Toast.makeText(
                            binding.root.context,
                            it.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    },
                ),
            onTapItemSubject
                .subscribe {
                    val bundle = Bundle()
                    bundle.putString("id", it.toString())
                    findNavController().navigate(
                        R.id.action_FirstFragment_to_SecondFragment,
                        bundle
                    )
                }
        )
    }

    private fun setupMovieListView() {
        binding.movieListView.adapter = MovieListAdapter(
            movies = movieList,
            onClick = { onTapItemSubject.onNext(it) }
        )
        binding.movieListView.layoutManager = LinearLayoutManager(binding.root.context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.clear()
    }
}