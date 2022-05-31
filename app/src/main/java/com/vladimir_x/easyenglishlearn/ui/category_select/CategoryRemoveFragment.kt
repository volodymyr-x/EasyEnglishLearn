package com.vladimir_x.easyenglishlearn.ui.category_select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.databinding.FragmentRemoveCategoryBinding
import javax.inject.Inject

class CategoryRemoveFragment : DialogFragment() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var _binding: FragmentRemoveCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by activityViewModels()

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
            viewModel.removeCategory(categoryName)
        }

        binding.rcfBtnNo.setOnClickListener {
            viewModel.cancelRemoving()
        }

        viewModel.removeCategoryLiveData.observe(
            viewLifecycleOwner
        )
        { closeDialog() }
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