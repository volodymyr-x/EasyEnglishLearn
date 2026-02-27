package com.volodymyr_x.easyenglishlearn.ui.category_edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentCategoryEditBinding
import com.volodymyr_x.easyenglishlearn.ui.category_select.CategorySelectContent
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryEditFragment : Fragment(R.layout.fragment_category_edit) {
    private var _binding: FragmentCategoryEditBinding? = null
    private val binding get() = _binding!!
    private var adapter: CategoryEditAdapter? = null
    private val viewModel: CategoryEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategoryEditBinding.bind(view)
        val oldCategoryName = requireArguments().getString(Constants.ARG_CATEGORY_NAME)
        setComposeContent(binding.root) {
            val wordList = viewModel.words.collectAsStateWithLifecycle(emptyList()).value
            CategoryEditContent(
                categoryName = oldCategoryName ?: "",
                categoryWords = wordList,
                action = viewModel::onAction
            )
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        words.collect { categoryList ->
                            adapter?.setWordList(categoryList)
                        }
                    }
                    launch {
                        categoryEditState.collect { state ->
                            when (state) {
                                is CategoryEditState.CloseScreenState -> {
                                    closeFragment()
                                }
                                is CategoryEditState.ShowMessage -> {
                                    showMessage(state.message)
                                }
                                is CategoryEditState.CurrentWord -> {

                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
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
        fun newInstance(categoryName: String?): Fragment {
            val args = Bundle()
            args.putString(Constants.ARG_CATEGORY_NAME, categoryName)
            val fragment: Fragment = CategoryEditFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
