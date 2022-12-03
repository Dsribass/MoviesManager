package com.example.moviesmanager.presentation.movies.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moviesmanager.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditMovieFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_movie, container, false)
    }
}