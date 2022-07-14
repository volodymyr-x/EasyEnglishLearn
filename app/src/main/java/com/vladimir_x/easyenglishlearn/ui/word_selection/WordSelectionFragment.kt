package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentWordSelectionBinding
import com.vladimir_x.easyenglishlearn.ui.ExerciseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordSelectionFragment : Fragment(R.layout.fragment_word_selection) {
    private var _binding: FragmentWordSelectionBinding? = null
    private val binding get() = _binding!!
    private var wordSelectionAdapter: WordSelectionAdapter? = null
    private val viewModel: WordSelectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordSelectionBinding.bind(view)
        initView()
        subscribeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        with(binding) {
            rvWordsChoice.apply {
                layoutManager = LinearLayoutManager(activity)
                wordSelectionAdapter = WordSelectionAdapter { checked, wordId ->
                    viewModel.onItemCheckBoxChange(checked, wordId)
                }
                adapter = wordSelectionAdapter
            }
            tvCategoryName.text = viewModel.categoryName
            btnStart.setOnClickListener { viewModel.onBtnStartClick() }
            cbChooseAll.setOnClickListener {
                viewModel.onChooseAllClick(cbChooseAll.isChecked)
            }
        }
    }

    private fun showMessage() {
        val message = getString(R.string.wsa_toast_min_words_count, Constants.MIN_CHECKED_WORD_QUANTITY)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeObservers() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    launch {
                        wordSelectionState.collect { state ->
                            when (state) {
                                is WordSelectionState.UpdateWords -> {
                                    wordSelectionAdapter?.submitList(state.words)
                                    chooseAllState(state.isChooseAllChecked)
                                }
                                is WordSelectionState.ShowMessage -> {
                                    showMessage()
                                }
                                is WordSelectionState.OpenDialog -> {
                                    showDialog(state.categoryName)
                                }
                                is WordSelectionState.StartExercise -> {
                                    startExercise(state.dto)
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDialog(categoryName: String) {
        childFragmentManager.setFragmentResultListener(
            Constants.EXERCISE_CHOICE_FRAGMENT,
            viewLifecycleOwner
        ) { _, bundle ->
            val exerciseChoiceDto =
                bundle.getSerializable(Constants.EXERCISE_CHOICE_KEY) as ExerciseChoiceDto
            viewModel.sendDTO(exerciseChoiceDto)
        }

        val dialogFragment = ExerciseChoiceFragment.newInstance(categoryName)
        dialogFragment.show(
            childFragmentManager,
            Constants.EXERCISE_CHOICE_FRAGMENT
        )

    }

    private fun chooseAllState(isChecked: Boolean) {
        binding.cbChooseAll.isChecked = isChecked
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