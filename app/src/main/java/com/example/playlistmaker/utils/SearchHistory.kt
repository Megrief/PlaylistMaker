package com.example.playlistmaker.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.activities.AudioplayerActivity
import com.example.playlistmaker.itunes.ITunesResponse
import com.example.playlistmaker.trackRecyclerView.Track
import com.example.playlistmaker.trackRecyclerView.TrackAdapter
import com.google.gson.Gson

class SearchHistory(context: Context, private val sharedPreferences: SharedPreferences, private val parentView: ViewGroup) {
    private val historyListView: RecyclerView = parentView.findViewById(R.id.history_list)
    private val clearButton: TextView = parentView.findViewById(R.id.clear_history)
    private val adapter = TrackAdapter {
        addToHistory(it)
        val jTrack = Gson().toJson(it, Track::class.java)
        sharedPreferences.edit().putString(Constants.K_TRACK.key, jTrack).apply()
        context.startActivity(Intent(context, AudioplayerActivity::class.java))
    }

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
        val jString = Gson().toJson(ITunesResponse(list.size, list), ITunesResponse::class.java)
        sharedPreferences.edit().putString(Constants.HISTORY_KEY.key, jString).apply()
    }

    private fun clearHistory() {
        sharedPreferences.edit().remove(Constants.HISTORY_KEY.key).apply()
        adapter.trackList.clear()
        parentView.visibility = GONE
        adapter.notifyDataSetChanged()
    }

    private fun getHistoryFromSharedPreferences(): List<Track> {
        return sharedPreferences.getString(Constants.HISTORY_KEY.key, null)?.let {
            Gson().fromJson(it, ITunesResponse::class.java).results
        } ?: emptyList()
    }

    fun refresh() {
        parentView.visibility = GONE
        adapter.notifyDataSetChanged()
    }
}