package com.vladimir_x.easyenglishlearn.category_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.ModelFactory
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentCategoryEditBinding
import com.vladimir_x.easyenglishlearn.model.Word

class CategoryEditFragment : Fragment() {
    private var _binding: FragmentCategoryEditBinding? = null
    private val binding get() = _binding!!

    private var viewModel: CategoryEditViewModel? = null
    private var adapter: CategoryEditAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryEditBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val oldCategoryName = requireArguments().getString(Constants.ARG_CATEGORY_NAME)
        viewModel = ModelFactory.getInstance(oldCategoryName ?: "")?.let {
            ViewModelProvider(this, it)[CategoryEditViewModel::class.java]
        }
        initView(oldCategoryName)
        subscribeToLiveData()
    }

    private fun getCorrectTitleId(oldCategoryName: String?): Int {
        return if (Constants.EMPTY_STRING == oldCategoryName) R.string.eca_tv_new_category
        else R.string.eca_tv_edit_category
    }


    private fun initView(oldCategoryName: String?) {
        with(binding) {
            adapter = CategoryEditAdapter(
                clickListener = { viewModel?.onItemClick(it) },
                removeClickListener = { viewModel?.onIconRemoveWordClick(it) }
            )
            rvCategoryEdit.adapter = adapter

            tvTitle.text = getString(getCorrectTitleId(oldCategoryName))
            etCategoryName.setText(oldCategoryName)
            btnSaveCategory.setOnClickListener {
                viewModel?.onBtnSaveCategoryClick(etCategoryName.text.toString())
            }
            btnSaveWord.setOnClickListener {
                viewModel?.onBtnSaveWordClick(
                    etCategoryName.text.toString(),
                    etLexeme.text.toString(),
                    etTranslation.text.toString()
                )
            }
            btnClean.setOnClickListener { viewModel?.onBtnCleanClick() }
        }
    }

    private fun subscribeToLiveData() {
        viewModel?.let { it ->
            it.wordsLiveData.observe(viewLifecycleOwner) { wordList: List<Word> ->
                adapter?.setWordList(wordList)
            }
            it.messageLiveData.observe(viewLifecycleOwner) { resId: Int? ->
                resId?.let(::showMessage)
            }
            it.fragmentCloseLiveData.observe(viewLifecycleOwner) { closeFragment() }
            it.currentWordLiveData.observe(viewLifecycleOwner) { (lexeme, translation) ->
                binding.etLexeme.setText(lexeme)
                binding.etTranslation.setText(translation)
            }
        }
    }

    private fun closeFragment() {
        requireActivity().onBackPressed()
    }

    private fun showMessage(@StringRes resId: Int) {
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
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