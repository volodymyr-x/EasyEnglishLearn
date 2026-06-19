package com.volodymyr_x.easyenglishlearn.ui.category_select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentCategorySelectBinding
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category_select) {
    private var callbacks: Callbacks? = null
    private var _binding: FragmentCategorySelectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()

    interface Callbacks {
        fun onCategorySelected(categoryName: String?)
        fun onCategoryEdit(categoryName: String?)
        fun onCategoryAdd()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategorySelectBinding.bind(view)
        setComposeContent(binding.root) {
            LaunchedEffect(binding.root) {
                viewModel.categoryAction.collect { action ->
                    when (action) {
                        CategoryAction.CreateNew -> {
                            callbacks?.onCategoryEdit("")
                        }
                        is CategoryAction.Edit -> {
                            callbacks?.onCategoryEdit(action.categoryName)
                        }
                        is CategoryAction.Selected -> {
                            callbacks?.onCategorySelected(action.categoryName)
                        }
                        is CategoryAction.Removed -> categoryRemovedYesClicked(action.categoryName)
                        CategoryAction.Add -> {
                            callbacks?.onCategoryAdd()
                        }
                    }
                }
            }
            val categoryState = viewModel.categoryState.collectAsStateWithLifecycle().value
            CategorySelectContent(
                state = categoryState, onEvent = { viewModel.onCategoryEvent(it) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun categoryRemovedYesClicked(categoryName: String) {
        showMessage(getString(R.string.category_removed, categoryName))
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoryFragment()
        }
    }
}
