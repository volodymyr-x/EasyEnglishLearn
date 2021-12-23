package com.vladimir_x.easyenglishlearn.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentWordQuizBinding
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.ArrayList

class WordQuizFragment : Fragment() {
    private var _binding: FragmentWordQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordQuizBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: WordQuizViewModel = ViewModelProvider(this)[WordQuizViewModel::class.java]
        subscribeToLiveData(viewModel)
        if (savedInstanceState == null) {
            val translationDirection =
                arguments?.getBoolean(Constants.TRANSLATION_DIRECTION) ?: false
            val wordList: List<Word> =
                arguments?.getParcelableArrayList<Word>(Constants.SELECTED_WORDS) as List<Word>
            viewModel.startExercise(wordList, translationDirection)
        }

        binding.wqfTvQuestion.text = viewModel.question
        binding.wqfRbFirst.text = viewModel.answers[0]
        binding.wqfRbFirst.setOnClickListener { viewModel.onAnswerChecked(viewModel.answers[0]) }
        binding.wqfRbSecond.text = viewModel.answers[1]
        binding.wqfRbSecond.setOnClickListener { viewModel.onAnswerChecked(viewModel.answers[1]) }
        binding.wqfRbThird.text = viewModel.answers[2]
        binding.wqfRbThird.setOnClickListener { viewModel.onAnswerChecked(viewModel.answers[2]) }
    }

    private fun subscribeToLiveData(viewModel: WordQuizViewModel) {
        viewModel.exerciseCloseLiveData.observe(viewLifecycleOwner) {
            closeFragment()
        }
        viewModel.messageLiveData.observe(viewLifecycleOwner) {
            it?.let { errorsCount: Int ->
                showMessage(errorsCount)
            }
        }
        viewModel.clearRadioGroupLiveData.observe(viewLifecycleOwner) {
            clearRadioGroup()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearRadioGroup() {
        _binding!!.wqfRgAnswers.clearCheck()
    }

    private fun closeFragment() {
        requireActivity().onBackPressed()
    }

    private fun showMessage(errorsCount: Int) {
        val message: String = if (errorsCount < 0) {
            getString(R.string.wrong_answer)
        } else {
            getString(R.string.errors_count, errorsCount)
        }
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(
            selectedWordList: ArrayList<Word>?,
            translationDirection: Boolean
        ) = WordQuizFragment().apply {
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