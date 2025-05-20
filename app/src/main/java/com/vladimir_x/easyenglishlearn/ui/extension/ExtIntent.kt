package com.vladimir_x.easyenglishlearn.ui.extension

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.util.ArrayList

inline fun <reified T : Parcelable> Intent.getParcelableArrayList(name: String): ArrayList<T>? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayListExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableArrayListExtra(name)
    }
