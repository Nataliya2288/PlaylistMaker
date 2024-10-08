package com.practicum.playlistmaker.search.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.KEY_FOR_PLAYER
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.root.listeners.BottomNavigationListener
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.SearchingViewModel
import com.practicum.playlistmaker.search.ui.models.TracksState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import org.koin.androidx.viewmodel.ext.android.viewModel


    class SearchFragment : Fragment() {

        private var bottomNavigationListener: BottomNavigationListener? = null
        private var _binding: FragmentSearchBinding? = null
        private val binding get() = _binding!!
        private var textFromSearchWidget = ""
        private val viewModel by viewModel<SearchingViewModel>()

        companion object {
            const val EDIT_TEXT_VALUE = "EDIT_TEXT_VALUE"
            private const val CLICK_DEBOUNCE_DELAY = 1000L
        }


        private var isClickAllowed = true

        private val adapter = TrackAdapter {
            if (clickDebounce()) {
                clickToTrackList(it)
            }
        }

        private val historyAdapter = TrackAdapter {
            if (clickDebounce()) {
                clickToHistoryTrackList(it)
            }
        }

        private lateinit var inputEditText: EditText
        private lateinit var clearButton: ImageView
        private lateinit var recyclerView: RecyclerView
        private lateinit var notFoundWidget: LinearLayout
        private lateinit var badConnectionWidget: LinearLayout
        private lateinit var updateButton: Button
        private lateinit var badConnectionTextView: TextView
        private lateinit var historyWidget: LinearLayout
        private lateinit var historyRecyclerView: RecyclerView
        private lateinit var clearHistoryButton: Button
        private lateinit var progressBar: ProgressBar

        override fun onAttach(context: Context) {
            super.onAttach(context)
            if (context is BottomNavigationListener) {
                bottomNavigationListener = context
            }
        }

        override fun onDetach() {
            super.onDetach()
            bottomNavigationListener = null
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentSearchBinding.inflate(inflater, container, false)
            return binding.root
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            inputEditText = binding.inputEditText
            clearButton = binding.clearIcon
            notFoundWidget = binding.notFoundWidget
            badConnectionWidget = binding.badConnectionWidget
            updateButton = binding.updateButton
            badConnectionTextView = binding.badConnection
            historyWidget = binding.historyWidget
            clearHistoryButton = binding.clearHistoryButton
            progressBar = binding.progressBar

            if (savedInstanceState != null) {
                inputEditText.setText(savedInstanceState.getString(EDIT_TEXT_VALUE, ""))
            }

            viewModel.tracksState.observe(viewLifecycleOwner) { TracksState ->
                render(TracksState)
            }

            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            recyclerView = binding.recyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            historyRecyclerView = binding.historyRecycleView
            historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            historyRecyclerView.adapter = historyAdapter

            viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
                historyAdapter.tracks = historyList
                historyAdapter.notifyDataSetChanged()
            }

            clearHistoryButton.setOnClickListener {
                viewModel.clearHistoryList()
                historyAdapter.notifyDataSetChanged()
                historyWidget.visibility = View.GONE
            }

            inputEditText.setOnFocusChangeListener { _, hasFocus ->
                historyWidget.visibility =
                    if (hasFocus && inputEditText.text.isEmpty() && viewModel.getHistoryList()
                            .isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }

            clearButton.setOnClickListener {
                inputEditText.setText("")
                adapter.tracks.clear()
                adapter.notifyDataSetChanged()
                inputMethodManager.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                historyWidget.visibility = View.GONE // Не показываем историю при плохом соединении
                badConnectionWidget.visibility = View.GONE
                notFoundWidget.visibility = View.GONE
            }

            updateButton.setOnClickListener {
                viewModel.searchRequest(inputEditText.text.toString())
            }
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    clearButton.visibility = clearButtonVisibility(s)
                    textFromSearchWidget = s?.toString() ?: ""

                    historyWidget.visibility =
                        if (inputEditText.hasFocus() && s?.isEmpty() == true && viewModel.getHistoryList()
                                .isNotEmpty()
                        ) View.VISIBLE else View.GONE

                    viewModel.searchDebounce(s?.toString() ?: "")
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            inputEditText.addTextChangedListener(textWatcher)
            inputEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.searchRequest(inputEditText.text.toString())
                    true
                } else {
                    false
                }
            }
            KeyboardVisibilityEvent.setEventListener(
                activity = requireActivity(),
                lifecycleOwner = viewLifecycleOwner
            ) { isOpen ->
                if (isOpen) {
                    onKeyboardVisibilityChanged(true)
                } else {
                    onKeyboardVisibilityChanged(false)
                }
            }
        }


        override fun onStop() {
            super.onStop()
            viewModel.saveHistoryList()
        }

        override fun onDestroyView() {
            viewModel.onDestroy()
            super.onDestroyView()
            _binding = null
        }

        override fun onPause() {
            super.onPause()
            if (inputEditText.text.toString().isEmpty()) {
                viewModel.refreshTrackState()
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putString(EDIT_TEXT_VALUE, textFromSearchWidget)
        }

        private fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun clickToTrackList(track: Track) {
            viewModel.addTrackToHistoryList(track)

            val intent = Intent(requireContext(),AudioPlayerActivity::class.java).apply {
                putExtra(KEY_FOR_PLAYER, track)
            }
            startActivity(intent)
        }

        private fun clickToHistoryTrackList(track: Track) {
            viewModel.transferTrackToTop(track)

            val intent = Intent(requireContext(),AudioPlayerActivity::class.java).apply {
                putExtra(KEY_FOR_PLAYER, track)
            }
            startActivity(intent)
        }

        private fun showPlaceholder(
            showNotFound: Boolean?,
            showBadConnection: Boolean,
            message: String = ""
        ) {
            notFoundWidget.visibility = if (showNotFound == true) View.VISIBLE else View.GONE
            badConnectionWidget.visibility = if (showBadConnection) View.VISIBLE else View.GONE
            badConnectionTextView.text = message

            // Очищаем список треков, если показываем плейсхолдеры
            if (showNotFound == true || showBadConnection) {
                updateTracksList(emptyList())
            }
        }

        private fun clickDebounce(): Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(CLICK_DEBOUNCE_DELAY)
                    isClickAllowed = true
                }
            }

            return current
        }

        private fun render(tracksState: TracksState) {
            showLoading(tracksState.isLoading)

            if (tracksState.isFailed != null) {
                val message = if (tracksState.isFailed) {
                    getString(R.string.server_error)
                } else {
                    getString(R.string.bad_connection)
                }
                showPlaceholder(showNotFound = false, showBadConnection = true, message = message)
            } else {
                handleTracks(tracksState.tracks)
            }
        }

        private fun handleTracks(tracks: List<Track>?) {
            if (tracks != null) {
                when {
                    tracks.isEmpty() && inputEditText.text.toString().isNotEmpty() -> {
                        showPlaceholder(showNotFound = true, showBadConnection = false)
                    }

                    else -> {
                        updateTracksList(tracks)
                        showPlaceholder(showNotFound = null, showBadConnection = false)
                    }
                }
            }
        }

        private fun updateTracksList(tracks: List<Track>) {
            adapter.tracks.clear()
            adapter.tracks.addAll(tracks)
            adapter.notifyDataSetChanged()
        }

        private fun showLoading(isLoading: Boolean) {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        private fun onKeyboardVisibilityChanged(isVisible: Boolean) {
            if (isVisible) {
                bottomNavigationListener?.toggleBottomNavigationViewVisibility(false)
            } else {
                bottomNavigationListener?.toggleBottomNavigationViewVisibility(true)
            }
        }
    }



