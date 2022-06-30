package com.vladimir_x.easyenglishlearn.ui.category_edit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentCategoryEditBinding
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
        initView(oldCategoryName)
        subscribeObservers()
    }

    private fun getCorrectTitleId(oldCategoryName: String?): Int {
        return if (Constants.EMPTY_STRING == oldCategoryName) R.string.eca_tv_new_category
        else R.string.eca_tv_edit_category
    }


    private fun initView(oldCategoryName: String?) {
        with(binding) {
            adapter = CategoryEditAdapter(
                clickListener = { viewModel.onItemClick(it) },
                removeClickListener = { viewModel.onIconRemoveWordClick(it) }
            )
            rvCategoryEdit.adapter = adapter

            tvTitle.text = getString(getCorrectTitleId(oldCategoryName))
            etCategoryName.setText(oldCategoryName)
            btnSaveCategory.setOnClickListener {
                viewModel.onBtnSaveCategoryClick(etCategoryName.text.toString())
            }
            btnSaveWord.setOnClickListener {
                viewModel.onBtnSaveWordClick(
                    etCategoryName.text.toString(),
                    etLexeme.text.toString(),
                    etTranslation.text.toString()
                )
            }
            btnClean.setOnClickListener { viewModel.onBtnCleanClick() }
        }
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
                                    binding.etLexeme.setText(state.pair.first)
                                    binding.etTranslation.setText(state.pair.second)
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
        requireActivity().onBackPressed()
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