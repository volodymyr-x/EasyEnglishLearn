package com.volodymyr_x.easyenglishlearn.ui.category_edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentCategoryEditBinding
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryEditFragment : Fragment(R.layout.fragment_category_edit) {
    private var _binding: FragmentCategoryEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryEditBinding.bind(view)
        val oldCategoryName = requireArguments().getString(Constants.ARG_CATEGORY_NAME)
        setComposeContent(binding.root) {
            LaunchedEffect(binding.root) {
                viewModel.categoryEditAction.collect { action ->
                    when (action) {
                        is CategoryEditAction.ShowMessage -> showMessage(action.message)
                        CategoryEditAction.CloseScreen -> closeFragment()
                    }
                }
            }
            val state = viewModel.categoryEditState.collectAsStateWithLifecycle().value
            CategoryEditContent(
                oldCategoryName = oldCategoryName ?: "",
                state = state,
                event = viewModel::onEvent
            )
        }
    }

    private fun closeFragment() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(categoryName: String? = null): Fragment {
            val args = Bundle()
            args.putString(Constants.ARG_CATEGORY_NAME, categoryName)
            val fragment: Fragment = CategoryEditFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
