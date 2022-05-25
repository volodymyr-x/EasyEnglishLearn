package com.vladimir_x.easyenglishlearn.ui.category_select

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.annotation.StringRes
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.databinding.FragmentCategorySelectBinding

class CategoryFragment : Fragment() {

    private var callbacks: Callbacks? = null
    private var viewModel: CategoryViewModel? = null
    private var adapter: CategoryAdapter? = null
    private var _binding: FragmentCategorySelectBinding? = null
    private val binding get() = _binding!!


    interface Callbacks {
        fun onCategorySelected(categoryName: String?)
        fun onCategoryEdit(categoryName: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorySelectBinding.inflate(
            inflater,
            container,
            false
        )
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        binding.rvCategorySelect.layoutManager = LinearLayoutManager(activity)
        adapter = CategoryAdapter(
            { viewModel?.onItemClick(it) },
            { viewModel?.onEditClick(it) },
            { viewModel?.onRemoveClick(it) }
        )
        binding.rvCategorySelect.adapter = adapter
        binding.fabCategoryAdd.setOnClickListener { viewModel?.onFabClick() }
        subscribeToLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun subscribeToLiveData() {
        viewModel?.categoriesLiveData?.observe(
            viewLifecycleOwner
        )
        { categoryList: List<String?> -> adapter?.setCategoryList(categoryList) }

        viewModel?.editCategoryLiveData?.observe(
            viewLifecycleOwner
        )
        { categoryName: String? -> callbacks?.onCategoryEdit(categoryName) }

        viewModel?.removeDialogLiveData?.observe(
            viewLifecycleOwner
        )
        {
            it?.let { categoryName: String ->
                showDialog(categoryName)
            }
        }

        viewModel?.openCategoryLiveData?.observe(
            viewLifecycleOwner
        )
        { categoryName: String? -> callbacks?.onCategorySelected(categoryName) }

        viewModel?.messageLiveData?.observe(
            viewLifecycleOwner
        )
        {
            it?.let { resId: Int ->
                showMessage(resId)
            }
        }
    }

    private fun showMessage(@StringRes resId: Int) {
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showDialog(categoryName: String) {
        val dialogFragment = CategoryRemoveFragment.newInstance(categoryName)
        dialogFragment.show(
            requireActivity().supportFragmentManager,
            Constants.DIALOG_REMOVE_CATEGORY
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return CategoryFragment()
        }
    }
}