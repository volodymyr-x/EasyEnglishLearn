package com.vladimir_x.easyenglishlearn.util

import android.content.Context
import androidx.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes resource: Int, vararg arguments: Any) : String {
        return if (arguments.isNotEmpty()) {
            context.resources.getString(resource, *arguments)
        } else {
            context.resources.getString(resource)
        }
    }
}