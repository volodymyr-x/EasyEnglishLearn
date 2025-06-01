package com.volodymyr_x.easyenglishlearn.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentConstructorBinding
import com.volodymyr_x.easyenglishlearn.ui.State
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConstructorFragment : Fragment(R.layout.fragment_constructor) {
    private var _binding: FragmentConstructorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConstructorViewModel by viewModels()
    private val newButtonListener = View.OnClickListener { v: View ->
        val letter = (v as Button).text.toString()
        viewModel.onNewButtonClick(letter)
        //binding.wcfGridContainer.removeView(v)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConstructorBinding.bind(view)
        initView()
        subscribeObservers()
        viewModel.prepareQuestionAndAnswers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        with(binding) {
            btnClean.setOnClickListener {
                viewModel.onButtonUndoClick()
            }
        }
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.exerciseState.collect {
                    when (it) {
                        is State.DataState<*> -> {
                            val dataDto = it.data as DataDto.ConstructorDto
                            createButtons(dataDto.letters)
                            fillTexFields(dataDto.question, dataDto.answer)
                        }
                        is State.ErrorState -> showError()
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

    private fun closeFragment() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun showFinalMessage(errorsCount: Int) {
        showMessage(getString(R.string.errors_count, errorsCount))
    }

    private fun showError() {
        showMessage(getString(R.string.wrong_answer))
    }

    private fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun createButtons(letters: List<Char>) {
        binding.gridContainer.removeAllViews()
        for (letter in letters) {
            val button = layoutInflater.inflate(
                R.layout.letter_button,
                binding.gridContainer,
                false
            ) as Button
            button.apply {
                text = letter.toString()
                setOnClickListener(newButtonListener)
            }
            binding.gridContainer.addView(button)
        }
    }

    private fun fillTexFields(question: String, answer: String) {
        with(binding) {
            tvQuestion.text = question
            tvAnswer.text = answer
        }
    }

    companion object {
        fun newInstance(
            selectedWordList: ArrayList<WordUI>,
            translationDirection: Boolean
        ) = ConstructorFragment().apply {
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
