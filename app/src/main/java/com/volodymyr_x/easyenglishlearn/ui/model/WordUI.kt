package com.volodymyr_x.easyenglishlearn.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordUI(
    val id: Long,
    val lexeme: String,
    val translation: String,
    val isChecked: Boolean = false
) : Parcelable
