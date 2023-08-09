package com.example.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.audioplayer.AudioplayerActivity
import com.example.playlistmaker.utils.ItemClickDebouncer
import com.example.playlistmaker.ui.search.activity.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.view_model.SearchScreeenState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : ComponentActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(LayoutInflater.from(this)) }
    private val onTrackClicked: (Track) -> Unit by lazy {
        {
            if (ItemClickDebouncer.clickDebounce()) {
                viewModel.addToHistory(it)
                (binding.historyList.adapter as TrackAdapter).notifyDataSetChanged()
                startActivity(Intent(this, AudioplayerActivity::class.java))
            }
        }
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                with(binding) {
                    if (s != null) savedValue = s.toString()
                    if (s.isNullOrBlank()) {
                        if (searchBar.hasFocus()) viewModel.showHistory()
                        viewModel.removeCallbacks()
                        clearButton.visibility = GONE
                    } else {
                        hideAll()
                        viewModel.search(savedValue)
                        clearButton.visibility = VISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }
    }

    // new
    private lateinit var viewModel: SearchViewModel
    ////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //new
        binding.trackList.adapter = TrackAdapter(onTrackClicked)
        binding.historyList.adapter = TrackAdapter(onTrackClicked)
        viewModel = ViewModelProvider(this, SearchViewModel.getSearchViewModelFactory())[SearchViewModel::class.java]
        viewModelConfig()
        //
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        configureSearchBar()
        configureClearButton()
        configureRefreshButton()
        configureClearHistoryButton()
    }
    // new
    private fun viewModelConfig() {
        viewModel.getSearchScreenStateLiveData().observe(this) {
            when (it) {
                is SearchScreeenState.Empty -> {
                    hideAll()
                }
                is SearchScreeenState.SearchHistory -> { showHistory(it.trackList) }
                is SearchScreeenState.IsLoading -> {
                    hideAll()
                    binding.progressBar.visibility = VISIBLE
                }
                is SearchScreeenState.NoInternetConnection -> { showNoInternetMessage() }
                is SearchScreeenState.NoResults -> { showNoResultsMessage() }
                is SearchScreeenState.SearchSuccess -> { showResults(it.trackList) }
            }
        }
    }
    ////
    // new
    private fun showResults(results: List<Track>) {
        with(binding.trackList.adapter as TrackAdapter) {
            trackList.clear()
            trackList.addAll(results)
            notifyDataSetChanged()
        }
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
        with(binding.historyList.adapter as TrackAdapter) {
            trackList.clear()
            trackList.addAll(history)
        }
        hideAll()
        binding.history.visibility = VISIBLE
    }
    ////

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_STATE, savedValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedValue = savedInstanceState.getString(SEARCH_BAR_STATE) ?: ""
    }

    private fun configureSearchBar() {
        with(binding.searchBar) {
            setText(savedValue)
            addTextChangedListener(textWatcher)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && savedValue.isEmpty()) viewModel.showHistory()
            }
        }
    }

    private fun configureClearButton() {
        with(binding) {
            clearButton.setOnClickListener {
                searchBar.setText("")
                savedValue = ""
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
                viewModel.clear()
                (binding.trackList.adapter as TrackAdapter).notifyDataSetChanged()
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
            (binding.historyList.adapter as TrackAdapter).let {
                it.trackList.clear()
                it.notifyDataSetChanged()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        savedValue = ""
    }

    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"

        private var savedValue: String = ""
    }

}