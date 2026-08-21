package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.databinding.FragmentConstructorBinding
import com.volodymyr_x.easyenglishlearn.ui.base_composables.LoadingScreen
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import com.volodymyr_x.easyenglishlearn.util.setComposeContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConstructorFragment : Fragment(R.layout.fragment_constructor) {
    private var _binding: FragmentConstructorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConstructorViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConstructorBinding.bind(view)
        setComposeContent(binding.root) {
            when (val screenState = viewModel.exerciseState.collectAsStateWithLifecycle().value) {
                is ConstructorState.LoadingState -> LoadingScreen()
                is ConstructorState.CompletedState -> ConstructorCompletedContent(
                    state = screenState.data,
                    closeAction = ::closeFragment
                )
                is ConstructorState.StageState -> ConstructorStageContent(
                    state = screenState.data,
                    event = viewModel::onEvent
                )
                is ConstructorState.UndoStageState -> ConstructorStageContent(
                    state = screenState.data,
                    event = viewModel::onEvent
                )
            }
        }
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
        ) = ConstructorFragment().apply {
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
