package com.vladimir_x.easyenglishlearn.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentQuizBinding
import com.vladimir_x.easyenglishlearn.ui.State
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels()
    private val clickListener = View.OnClickListener {
        viewModel.onAnswerChecked((it as RadioButton).text)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizBinding.bind(view)
        initView()
        subscribeObservers()
        viewModel.prepareQuestionAndAnswers()
    }

    private fun initView() {
        with(binding) {
            rbFirst.setOnClickListener(clickListener)
            rbSecond.setOnClickListener(clickListener)
            rbThird.setOnClickListener(clickListener)
        }
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.exerciseState.collect {
                    clearRadioGroup()
                    when (it) {
                        is State.DataState<*> -> {
                            val dataDto = it.data as DataDto.QuizDto
                            fillFields(dataDto.question, dataDto.answers)
                        }
                        is State.ErrorState -> showErrorMessage()
                        is State.CompletedState<*> -> {
                            showFinalMessage(it.data as Int)
                            closeFragment()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearRadioGroup() {
        binding.rgAnswers.clearCheck()
    }

    private fun closeFragment() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showFinalMessage(errorsCount: Int) {
        showMessage(getString(R.string.errors_count, errorsCount))
    }

    private fun showErrorMessage() {
        showMessage(getString(R.string.wrong_answer))
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun fillFields(question: String, answers: List<String>) {
        with(binding) {
            tvQuestion.text = question
            rbFirst.text = answers[0]
            rbSecond.text = answers[1]
            rbThird.text = answers[2]
        }
    }

    companion object {
        fun newInstance(
            selectedWordList: ArrayList<WordUI>,
            translationDirection: Boolean
        ) = QuizFragment().apply {
            arguments = createBundle(selectedWordList, translationDirection)
        }

        private fun createBundle(
            selectedWordList: ArrayList<WordUI>,
            translationDirection: Boolean
        ) = Bundle().apply {
            putParcelableArrayList(Constants.SELECTED_WORDS, selectedWordList)
            putBoolean(Constants.TRANSLATION_DIRECTION, translationDirection)
        }
    }
}
