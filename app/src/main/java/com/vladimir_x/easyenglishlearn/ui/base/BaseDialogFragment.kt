package com.vladimir_x.easyenglishlearn.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerDialogFragment

abstract class BaseDialogFragment<VM : ViewModel> : DaggerDialogFragment() {

    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = provideViewModel()
    }
}