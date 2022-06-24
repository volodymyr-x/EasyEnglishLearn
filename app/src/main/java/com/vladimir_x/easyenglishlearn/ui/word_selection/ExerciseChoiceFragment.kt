package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.FragmentExerciseChoiceBinding

class ExerciseChoiceFragment : DialogFragment(R.layout.fragment_exercise_choice) {
    private var _binding: FragmentExerciseChoiceBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseChoiceBinding.bind(view)
        initView()
    }

    private fun initView() {
        with(binding) {
            ecfBtnQuiz.setOnClickListener {
                prepareFragmentResult(getDto(Constants.WORD_QUIZ))
                closeDialog()
            }
            ecfBtnConstructor.setOnClickListener {
                prepareFragmentResult(getDto(Constants.WORD_CONSTRUCTOR))
                closeDialog()
            }
            ecfBtnCancel.setOnClickListener { closeDialog() }
        }
    }

    private fun prepareFragmentResult(dto: ExerciseChoiceDto) {
        parentFragmentManager.setFragmentResult(
            Constants.EXERCISE_CHOICE_FRAGMENT,
            bundleOf(Constants.EXERCISE_CHOICE_KEY to dto)
        )
    }

    private fun getDto(@Constants.Exercises exerciseType: String): ExerciseChoiceDto =
        ExerciseChoiceDto(
            isFromEnglish(),
            exerciseType
        )

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