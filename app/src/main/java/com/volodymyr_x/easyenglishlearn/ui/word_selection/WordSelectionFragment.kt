package com.volodymyr_x.easyenglishlearn.ui.word_selection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentWordSelectionBinding
import com.volodymyr_x.easyenglishlearn.ui.ExerciseActivity
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordSelectionFragment : Fragment(R.layout.fragment_word_selection) {
    private var _binding: FragmentWordSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WordSelectionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWordSelectionBinding.bind(view)
        setComposeContent(binding.root) {
            LaunchedEffect(binding.root) {
                viewModel.wordSelectionAction.collect { action ->
                    when (action) {
                        WordSelectionAction.ShowMessage -> showMessage()
                        is WordSelectionAction.StartExercise -> startExercise(action.dto)
                    }
                }
            }
            val wordSelectionState =
                viewModel.screenState.collectAsStateWithLifecycle().value
            WordSelectionContent(
                state = wordSelectionState,
                action = viewModel::onAction
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMessage() {
        val message =
            getString(R.string.wsa_toast_min_words_count, Constants.MIN_CHECKED_WORD_QUANTITY)
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun startExercise(dto: WordSelectionDto) {
        val intent = Intent(activity, ExerciseActivity::class.java)
        intent.putExtra(Constants.EXERCISE_TYPE, dto.exercise)
        intent.putParcelableArrayListExtra(Constants.SELECTED_WORDS, dto.selectedWordList)
        intent.putExtra(Constants.IS_LEXEME_TO_TRANSLATION, dto.isTranslationDirection)
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
