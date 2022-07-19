package com.vladimir_x.easyenglishlearn.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentConstructorBinding
import com.vladimir_x.easyenglishlearn.ui.State
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConstructorFragment : Fragment(R.layout.fragment_constructor) {
    private var _binding: FragmentConstructorBinding? = null
    private val binding get() = _binding!!
    private var constructorAdapter: ConstructorAdapter? = null
    private val viewModel: ConstructorViewModel by viewModels()

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
            rvConstructor.apply {
                layoutManager = FlexboxLayoutManager(activity).apply {
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                    justifyContent = JustifyContent.CENTER
                }
                constructorAdapter = ConstructorAdapter {
                    viewModel.onLetterButtonClick(it)
                }
                adapter = constructorAdapter
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
                            //createButtons(dataDto.letters)
                            constructorAdapter?.submitList(dataDto.letters)
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
        requireActivity().onBackPressed()
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