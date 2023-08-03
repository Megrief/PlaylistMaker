package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.adapter.TrackAdapter
import com.example.playlistmaker.ui.audioplayer.AudioplayerActivity
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.ui.utils.ItemClickDebouncer
import com.example.playlistmaker.ui.utils.SearchRequestDebouncer

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(LayoutInflater.from(this)) }
    private val onTrackClicked: (Track) -> Unit by lazy {
        {
            val storageInteractorTrack = Creator.createStorageInteractorTrack(applicationContext)
            if (itemClickDebouncer.clickDebounce()) {
                searchHistory.addToHistory(it)
                storageInteractorTrack.save(TRACK, it)
                startActivity(Intent(this, AudioplayerActivity::class.java))
            }
        }
    }
    private val adapter by lazy { TrackAdapter(onTrackClicked) }
    private val itemClickDebouncer = ItemClickDebouncer()
    private val searchRequestDebouncer = SearchRequestDebouncer()
    private val searchHistory by lazy { SearchHistory(binding.history, applicationContext, onTrackClicked) }
    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                if (s != null) savedValue = s.toString()
                searchHistory.showHistory(s.toString(), binding.searchBar.hasFocus())
                val searchRequest = Runnable {
                    binding.progressBar.visibility = VISIBLE
                    getResults(savedValue)
                }
                if (savedValue.isNotBlank()) {
                    binding.trackList.visibility = GONE
                    binding.somethingWrong.visibility = GONE
                    searchRequestDebouncer.searchDebounce(searchRequest)
                }
            }

            override fun afterTextChanged(s: Editable?) { }
        }
    }
    private val storageInteractorList by lazy { Creator.createStorageInteractorList(applicationContext) }
    private val searchInteractor by lazy { Creator.createSearchInteractor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        searchHistory.refresh()
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        configureSearchBar()
        configureTrackListView()
        configureClearButton()
        configureRefreshButton()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_STATE, savedValue)
        if (trackList.isNotEmpty()) {
            storageInteractorList.save(TRACK_LIST, trackList)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedValue = savedInstanceState.getString(SEARCH_BAR_STATE) ?: ""
        val savedList = savedInstanceState.getString(TRACK_LIST)
        if (savedList != null) {
            storageInteractorList.get(TRACK_LIST) {
                trackList.addAll(it)
            }
            adapter.notifyDataSetChanged()
            binding.trackList.visibility = VISIBLE
        }
    }

    private fun getResults(message: String) {
        searchInteractor.search(message) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                binding.progressBar.visibility = GONE
                trackList.clear()

                when {
                    it == null -> onError()
                    it.isEmpty() -> onEmpty()
                    else -> {
                        trackList.addAll(it)
                        adapter.notifyDataSetChanged()
                        binding.trackList.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun onEmpty() {
        with(binding) {
            somethingWrongImage.setImageResource(R.drawable.nothing_found)
            somethingWrongMessage.setText(R.string.nothing_found)
            history.visibility = GONE
            somethingWrong.visibility = VISIBLE
            refresh.visibility = GONE // на всякий случай
        }

    }

    private fun onError() {
        with(binding) {
            somethingWrongImage.setImageResource(R.drawable.no_internet)
            somethingWrongMessage.setText(R.string.no_internet)
            history.visibility = GONE
            refresh.visibility = VISIBLE
            somethingWrong.visibility = VISIBLE
        }

    }

    private fun configureSearchBar() {
        with(binding.searchBar) {
            setText(savedValue)
            addTextChangedListener(textWatcher)
            setOnFocusChangeListener { _, hasFocus ->
                searchHistory.showHistory(savedValue, hasFocus)
            }
        }

    }

    private fun configureTrackListView() {
        adapter.trackList = trackList
        binding.trackList.adapter = adapter
    }

    private fun configureClearButton() {
        with(binding) {
            clearButton.setOnClickListener {
                searchBar.setText("")
                savedValue = ""
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)

                Companion.trackList.clear()
                adapter.notifyDataSetChanged()
                searchHistory.showHistory(savedValue, hasFocus = true)
                trackList.visibility = GONE
                somethingWrong.visibility = GONE
            }
        }

    }

    private fun configureRefreshButton() {
        with(binding) {
            refresh.setOnClickListener {
                somethingWrong.visibility = GONE
                refresh.visibility = GONE
                getResults(savedValue)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        savedValue = ""
    }

    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
        const val TRACK_LIST = "TRACK_LIST"
        const val TRACK = "TRACK"

        private var savedValue: String = ""
        private val trackList = mutableListOf<Track>()
    }

}