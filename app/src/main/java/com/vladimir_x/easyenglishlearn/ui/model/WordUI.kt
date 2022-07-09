package com.vladimir_x.easyenglishlearn.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordUI(
    val id: Long = 0,
    val lexeme: String,
    val translation: String,
    var isChecked: Boolean = false
) : Comparable<WordUI>, Parcelable {

    override fun compareTo(other: WordUI): Int {
        return id.compareTo(other.id)
    }
}