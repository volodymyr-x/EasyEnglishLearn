package com.vladimir_x.easyenglishlearn.ui

import androidx.fragment.app.Fragment
import com.vladimir_x.easyenglishlearn.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return AboutFragment()
        }
    }
}