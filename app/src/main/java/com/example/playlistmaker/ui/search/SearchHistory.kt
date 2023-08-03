package com.example.playlistmaker.ui.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.adapter.TrackAdapter
import com.example.playlistmaker.creator.Creator

class SearchHistory(
    private val historyViewGroup: ViewGroup,
    context: Context,
    onTrackClicked: (Track) -> Unit
) {
    private val historyListView: RecyclerView = historyViewGroup.findViewById(R.id.history_list)
    private val clearButton: TextView = historyViewGroup.findViewById(R.id.clear_history)
    private val adapter = TrackAdapter(onTrackClicked)
    private val storageInteractor = Creator.createStorageInteractorList(context)

    init {
        historyListView.adapter = adapter
        storageInteractor.get(HISTORY_KEY) { adapter.trackList = it.toMutableList() }
        adapter.notifyDataSetChanged()
        clearButton.setOnClickListener { clearHistory() }
    }

    fun addToHistory(track: Track) {
        storageInteractor.get(HISTORY_KEY) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                with(it.toMutableList()) {
                    remove(track)
                    add(0, track)
                    organize(this)
                }
            }
        }
    }

    fun showHistory(s: String, hasFocus: Boolean) {
        historyViewGroup.isVisible = s.isBlank() && hasFocus && adapter.trackList.isNotEmpty()
    }

    private fun organize(list: MutableList<Track>) {
        if (list.size > 10) list.removeLast()
        adapter.trackList = list
        adapter.notifyDataSetChanged()
        storageInteractor.save(HISTORY_KEY, list)
    }

    private fun clearHistory() {
        storageInteractor.save(HISTORY_KEY, emptyList())
        adapter.trackList.clear()
        adapter.notifyDataSetChanged()
        historyViewGroup.visibility = GONE
    }

    fun refresh() {
        historyViewGroup.visibility = GONE
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
    }
}