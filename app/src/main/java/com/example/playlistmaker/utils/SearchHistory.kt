package com.example.playlistmaker.utils

import android.content.SharedPreferences
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.itunes.ITunesResponse
import com.example.playlistmaker.trackRecyclerView.Track
import com.example.playlistmaker.trackRecyclerView.TrackAdapter
import com.google.gson.Gson

class SearchHistory(
    private val sharedPreferences: SharedPreferences,
    private val parentView: ViewGroup,
    onTrackClicked: (Track) -> Unit
) {
    private val historyListView: RecyclerView = parentView.findViewById(R.id.history_list)
    private val clearButton: TextView = parentView.findViewById(R.id.clear_history)
    private val adapter = TrackAdapter(onTrackClicked)

    init {
        historyListView.adapter = adapter
        adapter.trackList = getHistoryFromSharedPreferences().toMutableList()
        adapter.notifyDataSetChanged()
        clearButton.setOnClickListener { clearHistory() }
    }

    fun addToHistory(track: Track) {
        getHistoryFromSharedPreferences().toMutableList().let {
            if (it.contains(track)) it.remove(track)
            it.add(0, track)
            organize(it)
        }
    }

    private fun organize(list: MutableList<Track>) {
        if (list.size > 10) list.removeLast()
        adapter.trackList = list
        adapter.notifyDataSetChanged()
        val jsonTrackListContainer = Gson().toJson(ITunesResponse(list.size, list), ITunesResponse::class.java)
        sharedPreferences.edit().putString(HISTORY_KEY, jsonTrackListContainer).apply()
    }

    private fun clearHistory() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
        parentView.visibility = GONE
    }

    private fun getHistoryFromSharedPreferences(): List<Track> {
        return sharedPreferences.getString(HISTORY_KEY, null)?.let {
            Gson().fromJson(it, ITunesResponse::class.java).results
        } ?: emptyList()
    }

    fun refresh() {
        parentView.visibility = GONE
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
    }
}