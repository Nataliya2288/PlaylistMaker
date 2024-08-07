package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsMedialibraryBinding
import com.practicum.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedialibraryPlaylistsFragment : Fragment() {
    companion object {

        fun newInstance() = MedialibraryPlaylistsFragment()
    }

    private val viewModel: MedialibraryPlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsMedialibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}