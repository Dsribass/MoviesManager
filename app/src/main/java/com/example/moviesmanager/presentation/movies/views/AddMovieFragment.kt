package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import com.example.moviesmanager.data.repository.MovieRepository
import com.example.moviesmanager.databinding.FragmentAddMovieBinding
import com.example.moviesmanager.model.Genre
import com.example.moviesmanager.model.Movie
import com.example.moviesmanager.utils.Factory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.time.LocalDate
import java.util.*

class AddMovieFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MovieRepository
    private val disposables = CompositeDisposable()

    private val onNameChangedSubject = BehaviorSubject.create<String>()
    private val onStudioChangedSubject = BehaviorSubject.create<String>()
    private val onDurationChangedSubject = BehaviorSubject.create<String>()
    private val onReleaseDateChangedSubject = BehaviorSubject.create<String>()
    private val onGenreSubject = BehaviorSubject.create<String>()
    private val onAddButtonSubject = BehaviorSubject.create<Unit>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        repository = Factory.makeMovieRepository(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generalSetup()
        setupObservables()
    }

    private fun setupObservables() {
        disposables.add(
            Observable.combineLatest(
                onNameChangedSubject,
                onStudioChangedSubject,
                onDurationChangedSubject,
                onReleaseDateChangedSubject,
                onGenreSubject
            ) { t1, t2, t3, t4, t5 ->
                t1.isNotEmpty() &&
                        t2.isNotEmpty() &&
                        t3.isNotEmpty() &&
                        t4.isNotEmpty() &&
                        t5.isNotEmpty()
            }
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe { binding.form.addMovieButton.isEnabled = it }
        )

        disposables.add(
            onAddButtonSubject
                .map {
                    with(binding.form) {
                        Movie(
                            id = UUID.randomUUID(),
                            name = addMovieName.text.toString(),
                            studio = addMovieStudio.text.toString(),
                            duration = addMovieDuration.text.toString().toInt(),
                            releaseDate = LocalDate.parse(addMovieReleaseDate.text.toString()),
                            genre = Genre.valueOf(addMovieGenres.selectedItem.toString()),
                            rate = null,
                        )
                    }
                }
                .subscribe {
                    repository.addMovie(it)
                        .subscribe {
                            findNavController().popBackStack()
                        }
                },
        )
    }

    private fun generalSetup() {
        fillGenresSpinner()

        with(binding.form) {
            addMovieName.addObserver { onNameChangedSubject.onNext(it) }
            addMovieStudio.addObserver { onStudioChangedSubject.onNext(it) }
            addMovieDuration.addObserver { onDurationChangedSubject.onNext(it) }
            addMovieReleaseDate.addObserver { onReleaseDateChangedSubject.onNext(it) }
            addMovieGenres.addObserver { onGenreSubject.onNext(it) }
            addMovieButton.setOnClickListener { onAddButtonSubject.onNext(Unit) }
        }
    }

    private fun fillGenresSpinner() {
        val genreList = ArrayAdapter<String>(
            binding.root.context,
            android.R.layout.simple_spinner_item,
            Genre.values().map { it.name }
        )

        binding.form.addMovieGenres.dropDownWidth = android.R.layout.simple_spinner_item
        binding.form.addMovieGenres.adapter = genreList
    }

    private fun EditText.addObserver(handler: (text: String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                handler(p0.toString())
            }
        })
    }

    private fun Spinner.addObserver(handler: (text: String) -> Unit) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                handler((Genre.values()[position]).name)
            }
        }
    }
}