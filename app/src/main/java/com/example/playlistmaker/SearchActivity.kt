package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private var savedValue: String = ""
    companion object {
        const val SEARCH_BAR_STATE = "SEARCH_BAR_STATE"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
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

        searchBar.setText(savedValue)


        searchBar.addTextChangedListener(textWatcher)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchBar.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchBar.windowToken, 0)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_BAR_STATE, savedValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedValue = savedInstanceState.getString(SEARCH_BAR_STATE) ?: ""
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}