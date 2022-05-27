package com.vladimir_x.easyenglishlearn.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentQuizBinding
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.ui.State
import com.vladimir_x.easyenglishlearn.ui.base.BaseFragment
import javax.inject.Inject

class QuizFragment : BaseFragment<QuizViewModel>() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val clickListener = View.OnClickListener {
        viewModel.onAnswerChecked((it as RadioButton).text)
    }

    override fun provideViewModel(): QuizViewModel =
        ViewModelProvider(this, factory)[QuizViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizBinding.bind(view)
        initView()
        initObservers(viewModel)

        if (savedInstanceState == null) {
            val translationDirection =
                arguments?.getBoolean(Constants.TRANSLATION_DIRECTION) ?: true
            val wordList: List<Word> =
                arguments?.getParcelableArrayList<Word>(Constants.SELECTED_WORDS) as? List<Word>
                    ?: emptyList()
            viewModel.startExercise(wordList, translationDirection)
        }
    }

    private fun initView() {
        with(binding) {
            rbFirst.setOnClickListener(clickListener)
            rbSecond.setOnClickListener(clickListener)
            rbThird.setOnClickListener(clickListener)
        }
    }

    private fun initObservers(viewModel: QuizViewModel) {
        with(viewModel) {
            exerciseState.observe(viewLifecycleOwner) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearRadioGroup() {
        binding.rgAnswers.clearCheck()
    }

    private fun closeFragment() {
        activity?.onBackPressed()
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
            selectedWordList: ArrayList<Word>?,
            translationDirection: Boolean
        ) = QuizFragment().apply {
            arguments = createBundle(selectedWordList, translationDirection)
        }

        private fun createBundle(
            selectedWordList: ArrayList<Word>?,
            translationDirection: Boolean
        ) = Bundle().apply {
            putParcelableArrayList(Constants.SELECTED_WORDS, selectedWordList)
            putBoolean(Constants.TRANSLATION_DIRECTION, translationDirection)
        }
    }
}