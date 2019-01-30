package com.example.vladimir.easyenglishlearn;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vladimir.easyenglishlearn.exercises.WordConstructorFragment;
import com.example.vladimir.easyenglishlearn.exercises.WordQuizFragment;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;

import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_TYPE;
import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_CONSTRUCTOR;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Intent intent = getIntent();
        String exerciseType = intent.getStringExtra(EXERCISE_TYPE);
        ArrayList<Word> selectedWordList = intent.getParcelableArrayListExtra(SELECTED_WORDS);
        boolean translationDirection = intent.getBooleanExtra(TRANSLATION_DIRECTION, true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.exercise_fragment_container);
        if (fragment == null) {
            switch (exerciseType) {
                case WORD_CONSTRUCTOR:
                    fragment = WordConstructorFragment
                            .newInstance(selectedWordList, translationDirection);
                    break;
                default:
                    fragment = WordQuizFragment
                            .newInstance(selectedWordList, translationDirection);
            }

            fm.beginTransaction()
                    .add(R.id.exercise_fragment_container, fragment)
                    .commit();
        }
    }
}
