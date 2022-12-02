package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentMovieDetailBinding
import com.example.moviesmanager.utils.Factory
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}