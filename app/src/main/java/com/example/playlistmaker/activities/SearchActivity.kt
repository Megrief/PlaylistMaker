package com.example.playlistmaker.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.itunes.ITunesResponse
import com.example.playlistmaker.itunes.ITunesSearch
import com.example.playlistmaker.trackRecyclerView.Track
import com.example.playlistmaker.trackRecyclerView.TrackAdapter
import com.example.playlistmaker.utils.ItemClickDebouncer
import com.example.playlistmaker.utils.SearchHistory
import com.example.playlistmaker.utils.SearchRequestDebouncer
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    private val clearButton by lazy { findViewById<ImageButton>(R.id.clear_button) }
    private val history by lazy { findViewById<LinearLayoutCompat>(R.id.history) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val refreshButton by lazy { findViewById<TextView>(R.id.refresh) }
    private val trackListView by lazy { findViewById<RecyclerView>(R.id.track_list) }
    private val searchBar by lazy { findViewById<EditText>(R.id.search_bar) }
    private val somethingWrong by lazy { findViewById<LinearLayoutCompat>(R.id.something_wrong) }
    private val somethingWrongImage by lazy { findViewById<ImageView>(R.id.something_wrong_image) }
    private val somethingWrongMessage by lazy { findViewById<TextView>(R.id.something_wrong_message) }
    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }

    private val onTrackClicked: (Track) -> Unit = {
        if (itemClickDebouncer.clickDebounce()) {
            searchHistory.addToHistory(it)
            val jTrack = Gson().toJson(it, Track::class.java)
            startActivity(
                Intent(this, AudioplayerActivity::class.java)
                    .putExtra(K_TRACK, jTrack)
            )
        }
    }
    private val adapter = TrackAdapter(onTrackClicked)
    private val itemClickDebouncer = ItemClickDebouncer()
    private val searchRequestDebouncer = SearchRequestDebouncer()
    private val searchHistory by lazy { SearchHistory(sharedPreferences, history, onTrackClicked) }
    private val sharedPreferences by lazy { getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE) }
    private val textWatcher by lazy {
        object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                if (!s.isNullOrEmpty()) savedValue = s.toString()
                showHistory(s.toString(), searchBar.hasFocus())

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
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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
            val listToJson = Gson().toJson(ITunesResponse(trackList.size, trackList), ITunesResponse::class.java)
            outState.putString(TRACK_LIST_STATE, listToJson)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedValue = savedInstanceState.getString(SEARCH_BAR_STATE) ?: ""
        val savedList = savedInstanceState.getString(TRACK_LIST_STATE)
        if (savedList != null) {
            trackList.addAll(Gson().fromJson(savedList, ITunesResponse::class.java).results)
            adapter.notifyDataSetChanged()
            trackListView.visibility = VISIBLE
        }
    }

    private fun getResults(message: String) {
        retrofit.create<ITunesSearch>().search(message).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                progressBar.visibility = GONE
                trackList.clear()
                if (response.isSuccessful) {
                    if (response.body()?.resultCount == 0) {
                        somethingWrongImage.setImageResource(R.drawable.nothing_found)
                        somethingWrongMessage.setText(R.string.nothing_found)
                        somethingWrong.visibility = VISIBLE
                    } else {
                        trackList.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                        trackListView.visibility = VISIBLE
                    }
                } else getError()
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                progressBar.visibility = GONE
                getError()
            }
        })
    }

    private fun getError() {
        somethingWrongImage.setImageResource(R.drawable.no_internet)
        somethingWrongMessage.setText(R.string.no_internet)
        if (history.isVisible) history.visibility = GONE
        refreshButton.visibility = VISIBLE
        somethingWrong.visibility = VISIBLE
    }

    private fun configureSearchBar() {
        searchBar.setText(savedValue)
        searchBar.addTextChangedListener(textWatcher)
        searchBar.setOnFocusChangeListener { _, hasFocus ->
            showHistory(savedValue, hasFocus)
        }
    }

    private fun configureTrackListView() {
        adapter.trackList = trackList
        trackListView.adapter = adapter
    }

    private fun configureClearButton() {
        clearButton.setOnClickListener {
            searchBar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
            trackList.clear()
            adapter.notifyDataSetChanged()
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

    private fun showHistory(s: String, hasFocus: Boolean) {
        history.visibility = if (
            s.isBlank()
            && hasFocus
            && sharedPreferences.contains(SearchHistory.HISTORY_KEY)
        ) VISIBLE else GONE
    }

    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
        const val TRACK_LIST_STATE = "TRACK_LIST_STATE"
        const val K_TRACK = "K_TRACK"
        const val BASE_URL = "https://itunes.apple.com"

        private var savedValue: String = ""
        private val trackList = mutableListOf<Track>()
    }

}