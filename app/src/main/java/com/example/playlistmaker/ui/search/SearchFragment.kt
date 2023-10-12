package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchScreeenState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()
    private var savedValue: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureLists()
        viewModelConfig()

        configureSearchBar()
        configureClearButton()
        configureRefreshButton()
        configureClearHistoryButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_STATE, savedValue)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedValue = savedInstanceState?.getString(SEARCH_BAR_STATE) ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        savedValue = ""
    }

    private fun configureLists() {
        with(binding) {
            val onTrackClicked = debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                lifecycleScope,
                false
            ) { track ->
                viewModel.addToHistory(track)
                findNavController().navigate(R.id.action_searchFragment_to_audioplayerActivity)
            }
            trackList.adapter = TrackAdapter(onTrackClicked)
            historyList.adapter = TrackAdapter(onTrackClicked)
        }
    }

    private fun viewModelConfig() {
        viewModel.searchScreenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchScreeenState.Empty -> {
                    hideAll()
                }
                is SearchScreeenState.SearchHistory -> {
                    showHistory(screenState.trackList)
                }
                is SearchScreeenState.IsLoading -> {
                    hideAll()
                    binding.progressBar.visibility = VISIBLE
                }
                is SearchScreeenState.NoInternetConnection -> { showNoInternetMessage() }
                is SearchScreeenState.NoResults -> { showNoResultsMessage() }
                is SearchScreeenState.SearchSuccess -> { showResults(screenState.trackList) }
            }
        }
    }

    private fun showResults(results: List<Track>) {
        (binding.trackList.adapter as TrackAdapter).setTrackList(results)
        hideAll()
        binding.trackList.visibility = VISIBLE
    }

    private fun hideAll() {
        with(binding) {
            history.visibility = GONE
            trackList.visibility = GONE
            somethingWrong.visibility = GONE
            progressBar.visibility = GONE
        }
    }

    private fun showNoInternetMessage() {
        hideAll()
        with(binding) {
            somethingWrongImage.setImageResource(R.drawable.no_internet)
            somethingWrongMessage.setText(R.string.no_internet)
            refresh.visibility = VISIBLE
            somethingWrong.visibility = VISIBLE
        }
    }

    private fun showNoResultsMessage() {
        hideAll()
        with(binding) {
            somethingWrongImage.setImageResource(R.drawable.nothing_found)
            somethingWrongMessage.setText(R.string.nothing_found)
            somethingWrong.visibility = VISIBLE
        }
    }

    private fun showHistory(history: List<Track>) {
        (binding.historyList.adapter as TrackAdapter).setTrackList(history)
        hideAll()
        binding.history.visibility = VISIBLE
    }

    private fun configureSearchBar() {
        with(binding.searchBar) {
            setText(savedValue)

            doOnTextChanged { s, _, _, _->
                savedValue = s?.toString() ?: ""
                if (s.isNullOrBlank()) {
                    if (binding.searchBar.hasFocus()) {
                        if (binding.history.isGone) {
                            hideAll()
                            viewModel.showHistory()
                        }
                    }
                    binding.clearButton.visibility = GONE
                } else {
                    hideAll()
                    binding.clearButton.visibility = VISIBLE
                }
                viewModel.search(savedValue)
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && savedValue.isEmpty()) {
                    viewModel.showHistory()
                }
            }
        }
    }

    private fun configureClearButton() {
        with(binding) {
            clearButton.setOnClickListener {
                searchBar.setText("")
                savedValue = ""
                val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
                viewModel.clear()
                (binding.trackList.adapter as TrackAdapter).setTrackList(emptyList())
                hideAll()
                viewModel.showHistory()
            }
        }
    }

    private fun configureRefreshButton() {
        with(binding) {
            refresh.setOnClickListener {
                hideAll()
                viewModel.search(savedValue)
            }
        }

    }

    private fun configureClearHistoryButton() {
        binding.clearHistory.setOnClickListener {
            hideAll()
            viewModel.clearHistory()
            (binding.historyList.adapter as TrackAdapter).setTrackList(emptyList())
        }
    }

    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}