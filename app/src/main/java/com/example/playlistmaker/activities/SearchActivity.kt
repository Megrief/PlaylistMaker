package com.example.playlistmaker.activities

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.itunes.ITunesResponse
import com.example.playlistmaker.itunes.ITunesSearch
import com.example.playlistmaker.trackRecyclerView.Track
import com.example.playlistmaker.trackRecyclerView.TrackAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    private val trackListView by lazy { findViewById<RecyclerView>(R.id.track_list) }
    private val adapter = TrackAdapter()
    private val searchBar by lazy { findViewById<EditText>(R.id.search_bar) }
    private val searchBarWatcher by lazy { object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //empty
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
            if (!s.isNullOrEmpty()) savedValue = s.toString()
        }

        override fun afterTextChanged(s: Editable?) {
            //empty
        }
    } }
    private val clearButton by lazy { findViewById<ImageButton>(R.id.clear_button) }
    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private val somethingWrong by lazy { findViewById<LinearLayoutCompat>(R.id.something_wrong) }
    private val somethingWrongImage by lazy { findViewById<ImageView>(R.id.something_wrong_image) }
    private val somethingWrongMessage by lazy { findViewById<TextView>(R.id.something_wrong_message) }
    private val refreshButton by lazy { findViewById<TextView>(R.id.refresh) }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create<ITunesSearch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar.setNavigationOnClickListener { finish() }

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
        if (savedList != null) trackList.addAll(Gson().fromJson(savedList, ITunesResponse::class.java).results)
    }
    private fun getResults() {
        service.search(savedValue).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>) {
                trackList.clear()
                if (response.isSuccessful) {
                    if (response.body()?.resultCount == 0) {
                        somethingWrongImage.setImageResource(R.drawable.nothing_found)
                        somethingWrongMessage.setText(R.string.nothing_found)
                        somethingWrong.makeVisible()
                    } else {
                        trackList.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                        trackListView.makeVisible()
                    }
                } else getError()
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                getError()
            }
        })
    }
    private fun View.makeInvisible() {
        if (this.isVisible) this.visibility = GONE
    }
    private fun View.makeVisible() {
        if (this.isGone) this.visibility = VISIBLE
    }
    private fun getError() {
        somethingWrongImage.setImageResource(R.drawable.no_internet)
        somethingWrongMessage.setText(R.string.no_internet)
        refreshButton.makeVisible()
        somethingWrong.makeVisible()
    }
    private fun configureSearchBar() {
        searchBar.setText(savedValue)
        searchBar.addTextChangedListener(searchBarWatcher)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackListView.makeInvisible()
                somethingWrong.makeInvisible()
                if (savedValue.isNotEmpty()) getResults()
            }
            false
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
            trackListView.makeInvisible()
            somethingWrong.makeInvisible()
        }
    }
    private fun configureRefreshButton() {
        refreshButton.setOnClickListener {
            somethingWrong.makeInvisible()
            refreshButton.makeInvisible()
            getResults()
        }
    }
    companion object {
        const val BASE_URL: String = "https://itunes.apple.com"
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
        const val TRACK_LIST_STATE = "TRACK_LIST_STATE"
        private var savedValue: String = ""
        private val trackList = mutableListOf<Track>()
    }
}