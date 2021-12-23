package com.vladimir_x.easyenglishlearn

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.AboutFragment

class AboutFragment : Fragment(R.layout.fragment_about) {

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return AboutFragment()
        }
    }
}