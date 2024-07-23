package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavouritesMedialibraryBinding
import com.practicum.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MedialibraryFavouritesFragment : Fragment() {
    companion object {
        fun newInstance()= MedialibraryFavouritesFragment ()
    }


    private val viewModel: MedialibraryFavouritesViewModel by viewModel()

    private var _binding: FragmentFavouritesMedialibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}