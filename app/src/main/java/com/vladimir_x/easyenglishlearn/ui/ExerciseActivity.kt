package com.vladimir_x.easyenglishlearn.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.databinding.ActivityExerciseBinding
import com.vladimir_x.easyenglishlearn.ui.exercises.ConstructorFragment
import com.vladimir_x.easyenglishlearn.ui.exercises.QuizFragment
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import com.vladimir_x.easyenglishlearn.ui.extension.getParcelableArrayList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val exerciseType = intent.getStringExtra(Constants.EXERCISE_TYPE)
        val selectedWordList =
            intent.getParcelableArrayList<WordUI>(Constants.SELECTED_WORDS) as ArrayList<WordUI>
        val translationDirection = intent.getBooleanExtra(Constants.TRANSLATION_DIRECTION, true)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(binding.exerciseFragmentContainer.id)
        if (fragment == null) {
            fragment = when (exerciseType) {
                Constants.WORD_CONSTRUCTOR -> ConstructorFragment
                    .newInstance(selectedWordList, translationDirection)
                else -> QuizFragment
                    .newInstance(selectedWordList, translationDirection)
            }
            fm.beginTransaction()
                .add(R.id.exercise_fragment_container, fragment)
                .commit()
        }
    }
}
