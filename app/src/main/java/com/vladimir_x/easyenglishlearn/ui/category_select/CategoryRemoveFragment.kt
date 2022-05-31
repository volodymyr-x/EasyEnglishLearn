package com.vladimir_x.easyenglishlearn.ui.category_select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.databinding.FragmentRemoveCategoryBinding

class CategoryRemoveFragment : DialogFragment() {
    private var _binding: FragmentRemoveCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRemoveCategoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val categoryName = requireArguments().getString(Constants.CATEGORY_NAME) ?: ""
        binding.rcfTvRemoveCategory.text = categoryName

        binding.rcfBtnYes.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                Constants.DIALOG_REMOVE_CATEGORY,
                bundleOf(Constants.RESULT_KEY to DialogResult.Yes)
            )
            closeDialog()
        }

        binding.rcfBtnNo.setOnClickListener {
            closeDialog()
        }
    }

    private fun closeDialog() {
        dismiss()
    }

    companion object {
        fun newInstance(categoryName: String?): DialogFragment {
            val args = Bundle()
            args.putString(Constants.CATEGORY_NAME, categoryName)
            val fragment: DialogFragment = CategoryRemoveFragment()
            fragment.arguments = args
            return fragment
        }
    }
}