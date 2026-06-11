package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentQuizBinding
import com.volodymyr_x.easyenglishlearn.ui.exercises.ExerciseState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizBinding.bind(view)
        setComposeContent(binding.root) {
            when (val screenState = viewModel.exerciseState.collectAsStateWithLifecycle().value) {
                is ExerciseState.LoadingState -> LoadingScreen()
                is ExerciseState.CompletedState -> QuizCompletedContent(
                    state = screenState.data,
                    closeAction = ::closeFragment
                )
                is ExerciseState.StageState -> QuizStageContent(
                    state = screenState.data,
                    answerAction = viewModel::onAnswerChecked
                )
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        /*viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.exerciseState.collect {
                    when (it) {
                        is State.DataState<*> -> {
                            val dataDto = it.data as DataDto.QuizDto
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
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun closeFragment() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
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
            putBoolean(Constants.IS_LEXEME_TO_TRANSLATION, translationDirection)
        }
    }
}
