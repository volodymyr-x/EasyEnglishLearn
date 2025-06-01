package com.volodymyr_x.easyenglishlearn.ui

import androidx.fragment.app.Fragment
import com.volodymyr_x.easyenglishlearn.R

class AboutFragment : Fragment(R.layout.fragment_about) {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return AboutFragment()
        }
    }
}
