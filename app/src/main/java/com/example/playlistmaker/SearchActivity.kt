package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class SearchActivity : AppCompatActivity() {
    private var savedValue: String = ""
    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val clearButton = findViewById<ImageButton>(R.id.clear_button)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (!s.isNullOrEmpty()) savedValue = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }

        if (savedInstanceState != null) {
            searchBar.setText(savedInstanceState.getString(SEARCH_BAR_STATE))
        }

        searchBar.addTextChangedListener(textWatcher)

        clearButton.setOnClickListener {
            searchBar.setText("")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_STATE, savedValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}