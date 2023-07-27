package com.example.playlistmaker.presentaion.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentaion.adapter.TrackAdapter
import com.example.playlistmaker.presentaion.ui.audioplayer.AudioplayerActivity
import com.example.playlistmaker.presentaion.utils.Creator
import com.example.playlistmaker.presentaion.utils.ItemClickDebouncer
import com.example.playlistmaker.presentaion.utils.SearchRequestDebouncer
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private val clearButton by lazy { findViewById<ImageButton>(R.id.clear_button) }
    private val history by lazy { findViewById<LinearLayoutCompat>(R.id.history) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val refreshButton by lazy { findViewById<TextView>(R.id.refresh) }
    private val searchBar by lazy { findViewById<EditText>(R.id.search_bar) }
    private val somethingWrong by lazy { findViewById<LinearLayoutCompat>(R.id.something_wrong) }
    private val somethingWrongImage by lazy { findViewById<ImageView>(R.id.something_wrong_image) }
    private val somethingWrongMessage by lazy { findViewById<TextView>(R.id.something_wrong_message) }
    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private val trackListView by lazy { findViewById<RecyclerView>(R.id.track_list) }

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
    private val searchHistory by lazy { SearchHistory(history, applicationContext, onTrackClicked) }
    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                if (s != null) savedValue = s.toString()
                searchHistory.showHistory(s.toString(), searchBar.hasFocus())
                val searchRequest = Runnable {
                    progressBar.visibility = VISIBLE
                    getResults(savedValue)
                }
                if (savedValue.isNotBlank()) {
                    trackListView.visibility = GONE
                    somethingWrong.visibility = GONE
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
        setContentView(R.layout.activity_search)

        searchHistory.refresh()
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
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
            trackListView.visibility = VISIBLE
        }
    }

    private fun getResults(message: String) {
        searchInteractor.search(message) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                progressBar.visibility = GONE
                trackList.clear()

                when {
                    it == null -> onError()
                    it.isEmpty() -> onEmpty()
                    else -> {
                        trackList.addAll(it)
                        adapter.notifyDataSetChanged()
                        trackListView.visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun onEmpty() {
        somethingWrongImage.setImageResource(R.drawable.nothing_found)
        somethingWrongMessage.setText(R.string.nothing_found)
        history.visibility = GONE
        somethingWrong.visibility = VISIBLE
        refreshButton.visibility = GONE // на всякий случай
    }

    private fun onError() {
        somethingWrongImage.setImageResource(R.drawable.no_internet)
        somethingWrongMessage.setText(R.string.no_internet)
        history.visibility = GONE
        refreshButton.visibility = VISIBLE
        somethingWrong.visibility = VISIBLE
    }

    private fun configureSearchBar() {
        searchBar.setText(savedValue)
        searchBar.addTextChangedListener(textWatcher)
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            searchHistory.showHistory(savedValue, hasFocus)
        }
    }

    private fun configureTrackListView() {
        adapter.trackList = trackList
        trackListView.adapter = adapter
    }

    private fun configureClearButton() {
        clearButton.setOnClickListener {
            searchBar.setText("")
            savedValue = ""
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
            trackList.clear()
            adapter.notifyDataSetChanged()
            searchHistory.showHistory(savedValue, hasFocus = true)
            trackListView.visibility = GONE
            somethingWrong.visibility = GONE
        }
    }

    private fun configureRefreshButton() {
        refreshButton.setOnClickListener {
            somethingWrong.visibility = GONE
            refreshButton.visibility = GONE
            getResults(savedValue)
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