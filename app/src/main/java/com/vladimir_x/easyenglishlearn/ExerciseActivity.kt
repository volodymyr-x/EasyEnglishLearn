package com.vladimir_x.easyenglishlearn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vladimir_x.easyenglishlearn.databinding.ActivityExerciseBinding
import com.vladimir_x.easyenglishlearn.exercises.WordConstructorFragment
import com.vladimir_x.easyenglishlearn.exercises.QuizFragment
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.ArrayList

class ExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        val exerciseType = intent.getStringExtra(Constants.EXERCISE_TYPE)
        val selectedWordList =
            intent.getParcelableArrayListExtra<Word>(Constants.SELECTED_WORDS) as ArrayList<Word>
        val translationDirection = intent.getBooleanExtra(Constants.TRANSLATION_DIRECTION, true)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.exercise_fragment_container)
        if (fragment == null) {
            fragment = when (exerciseType) {
                Constants.WORD_CONSTRUCTOR -> WordConstructorFragment
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