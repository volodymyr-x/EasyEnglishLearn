package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentExerciseChoiceBinding
import com.vladimir_x.easyenglishlearn.ui.base.BaseDialogFragment
import javax.inject.Inject

class ExerciseChoiceFragment : BaseDialogFragment<WordSelectionViewModel>() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private var _binding: FragmentExerciseChoiceBinding? = null
    private val binding get() = _binding!!

    override fun provideViewModel(): WordSelectionViewModel =
        ViewModelProvider(requireActivity(), factory)[WordSelectionViewModel::class.java]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseChoiceBinding.inflate(
            inflater,
            container,
            false
        )
        val categoryName = requireArguments().getString(Constants.ARG_CATEGORY_NAME) ?: ""
        initView()

        viewModel.closeDialogLiveData.observe(viewLifecycleOwner) { closeDialog() }
        return binding.root
    }

    private fun initView() {
        with(binding) {
            ecfRgTranslationDirection.setOnCheckedChangeListener { _, checkedId ->
                viewModel.onDirectionChanged(checkedId)
            }
            ecfBtnQuiz.setOnClickListener { viewModel.onBtnQuizClick(isFromEnglish()) }
            ecfBtnConstructor.setOnClickListener { viewModel.onBtnConstructorClick(isFromEnglish()) }
            ecfBtnCancel.setOnClickListener { viewModel.onBtnCancelClick() }
        }
    }

    private fun closeDialog() {
        dismiss()
    }

    private fun isFromEnglish(): Boolean =
        binding.ecfRgTranslationDirection.checkedRadioButtonId == R.id.ecf_rb_en_ru

    companion object {
        @JvmStatic
        fun newInstance(categoryName: String?): DialogFragment {
            val args = Bundle()
            args.putString(Constants.ARG_CATEGORY_NAME, categoryName)
            val fragment: DialogFragment = ExerciseChoiceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}