package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.ui.ExerciseActivity
import com.vladimir_x.easyenglishlearn.util.ModelFactory
import com.vladimir_x.easyenglishlearn.databinding.FragmentWordSelectionBinding
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.ui.category_edit.CategoryEditViewModel

class WordSelectionFragment : Fragment() {
    private var _binding: FragmentWordSelectionBinding? = null
    private val binding get() = _binding!!
    private var wordSelectionAdapter: WordSelectionAdapter? = null
    private var viewModel: WordSelectionViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordSelectionBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName = requireArguments().getString(Constants.ARG_CATEGORY_NAME) ?: ""
        /*viewModel = ModelFactory.getInstance(categoryName)?.let {
            ViewModelProvider(this, it)[WordSelectionViewModel::class.java]
        }*/
        viewModel = ViewModelProvider(this)[WordSelectionViewModel::class.java]
        viewModel?.init(categoryName)
        initView()
        subscribeToLiveData()
    }

    private fun initView() {
        with(binding) {
            rvWordsChoice.apply {
                layoutManager = LinearLayoutManager(activity)
                wordSelectionAdapter = WordSelectionAdapter { checked, word ->
                    viewModel?.onItemCheckBoxChange(checked, word)
                }
                adapter = wordSelectionAdapter
            }
            tvCategoryName.text = viewModel?.categoryName
            btnStart.setOnClickListener { viewModel?.onBtnStartClick() }
            cbChooseAll.setOnCheckedChangeListener { _, checked ->
                viewModel?.onChooseAllChange(checked)
            }
        }
    }

    private fun showMessage(@StringRes resId: Int) {
        val message = getString(resId, Constants.ANSWERS_COUNT)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToLiveData() {
        viewModel?.let {
            it.wordsLiveData.observe(viewLifecycleOwner) { wordList: List<Word> ->
                wordSelectionAdapter?.setWordList(wordList)
            }

            it.messageLiveData.observe(viewLifecycleOwner) { resId ->
                resId?.let { showMessage(resId) }
            }

            it.choiceDialogLiveData.observe(viewLifecycleOwner) { categoryName ->
                categoryName?.let { showDialog(categoryName) }
            }

            it.selectedWordsLiveData.observe(viewLifecycleOwner) { dto ->
                dto?.let { startExercise(dto) }
            }
        }
    }

    private fun showDialog(categoryName: String) {
        val dialogFragment = ExerciseChoiceFragment.newInstance(categoryName)
        dialogFragment.show(
            requireActivity().supportFragmentManager,
            Constants.EXERCISE_CHOICE_FRAGMENT
        )
    }

    private fun startExercise(dto: WordSelectionDto) {
        val intent = Intent(activity, ExerciseActivity::class.java)
        intent.putExtra(Constants.EXERCISE_TYPE, dto.exercise)
        intent.putParcelableArrayListExtra(Constants.SELECTED_WORDS, dto.selectedWordList)
        intent.putExtra(Constants.TRANSLATION_DIRECTION, dto.isTranslationDirection)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String?): Fragment {
            val args = Bundle()
            args.putString(Constants.ARG_CATEGORY_NAME, categoryName)
            val fragment: Fragment = WordSelectionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}