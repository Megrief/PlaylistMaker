package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun getCorrectTracks(num: String): String {
    return when {
        num.last() == '1'
                && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трек"
        num.last() in '2'..'4'
                && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трека"
        else -> "треков"
    }
}

fun getCorrectTime(num: Long): String = buildString {
    val length = SimpleDateFormat("mm", Locale.getDefault()).format(num)
    append(length)
    append(" ")
    append(
        when {
            length.last() == '1'
                    && if (length.length > 1) length[length.lastIndex - 1] != '1' else true -> "минута"
            length.last() in '2'..'4'
                    && if (length.length > 1) length[length.lastIndex - 1] != '1' else true -> "минуты"
            else -> "минут"
        }
    )
}