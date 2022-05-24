package com.vladimir_x.easyenglishlearn.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.ui.State
import com.vladimir_x.easyenglishlearn.databinding.FragmentConstructorBinding
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.ArrayList

class ConstructorFragment : Fragment() {
    private var viewModel: ConstructorViewModel? = null
    private var _binding: FragmentConstructorBinding? = null
    private val binding get() = _binding!!
    private val newButtonListener = View.OnClickListener { v: View ->
        val letter = (v as Button).text.toString()
        viewModel?.onNewButtonClick(letter)
        //binding.wcfGridContainer.removeView(v)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstructorBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ConstructorViewModel::class.java]
        if (savedInstanceState == null) {
            val translationDirection =
                arguments?.getBoolean(Constants.TRANSLATION_DIRECTION) ?: true
            val wordList =
                arguments?.getParcelableArrayList<Word>(Constants.SELECTED_WORDS) as List<Word>
            viewModel?.startExercise(wordList, translationDirection)
        }
        initView()
        subscribeToLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        with(binding) {
            btnClean.setOnClickListener {
                viewModel?.onButtonUndoClick()
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel?.exerciseState?.observe(viewLifecycleOwner) {
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
            selectedWordList: ArrayList<Word>,
            translationDirection: Boolean
        ) = ConstructorFragment().apply {
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