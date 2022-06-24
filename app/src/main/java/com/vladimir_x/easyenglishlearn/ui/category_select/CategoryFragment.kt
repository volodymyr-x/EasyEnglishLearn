package com.vladimir_x.easyenglishlearn.ui.category_select

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentCategorySelectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category_select) {
    private var callbacks: Callbacks? = null
    private var adapter: CategoryAdapter? = null
    private var _binding: FragmentCategorySelectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()

    interface Callbacks {
        fun onCategorySelected(categoryName: String?)
        fun onCategoryEdit(categoryName: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCategorySelectBinding.bind(view)
        binding.rvCategorySelect.layoutManager = LinearLayoutManager(activity)
        adapter = CategoryAdapter(
            { viewModel.onItemClick(it) },
            { viewModel.onEditClick(it) },
            { viewModel.onRemoveClick(it) }
        )
        binding.rvCategorySelect.adapter = adapter

        binding.rvCategorySelect.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabCategoryAdd.visibility == View.VISIBLE) {
                    binding.fabCategoryAdd.hide()
                } else if (dy < 0 && binding.fabCategoryAdd.visibility != View.VISIBLE) {
                    binding.fabCategoryAdd.show()
                }
            }
        })

        binding.fabCategoryAdd.setOnClickListener { viewModel.onFabClick() }
        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun subscribeObservers() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        categories.collect { categoryList ->
                            adapter?.setCategoryList(categoryList)
                        }
                    }
                    launch {
                        categoryState.collect { state ->
                            when (state) {
                                is CategorySelectState.OpenCategory -> {
                                    callbacks?.onCategorySelected(state.data)
                                }
                                is CategorySelectState.EditCategory -> {
                                    callbacks?.onCategoryEdit(state.data)
                                }
                                is CategorySelectState.RemoveCategory -> {
                                    showDialog(state.data)
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(categoryName: String) {
        childFragmentManager.setFragmentResultListener(
            Constants.DIALOG_REMOVE_CATEGORY,
            viewLifecycleOwner
        ) { _, bundle ->
            when (bundle.getSerializable(Constants.RESULT_KEY) as DialogResult) {
                DialogResult.Yes -> dialogYesClicked(categoryName)
                else -> {}
            }
        }

        val dialogFragment = CategoryRemoveFragment.newInstance(categoryName)
        dialogFragment.show(
            childFragmentManager,
            Constants.DIALOG_REMOVE_CATEGORY
        )
    }

    private fun dialogYesClicked(categoryName: String) {
        viewModel.removeCategory(categoryName)
        showMessage(getString(R.string.category_removed, categoryName))
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoryFragment()
        }
    }
}