package com.vladimir_x.easyenglishlearn;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.vladimir_x.easyenglishlearn.R;
import com.vladimir_x.easyenglishlearn.exercises.WordConstructorFragment;
import com.vladimir_x.easyenglishlearn.exercises.WordQuizFragment;
import com.vladimir_x.easyenglishlearn.model.Word;

import java.util.ArrayList;

import static com.vladimir_x.easyenglishlearn.Constants.EXERCISE_TYPE;
import static com.vladimir_x.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.vladimir_x.easyenglishlearn.Constants.TRANSLATION_DIRECTION;
import static com.vladimir_x.easyenglishlearn.Constants.WORD_CONSTRUCTOR;

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
