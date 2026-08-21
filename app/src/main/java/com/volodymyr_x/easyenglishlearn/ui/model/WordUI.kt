package com.volodymyr_x.easyenglishlearn.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize // todo remove this annotation, because it is not used in the app
@Serializable
data class WordUI(
    val id: Long,
    val lexeme: String,
    val translation: String,
    val isChecked: Boolean = false
) : Parcelable
