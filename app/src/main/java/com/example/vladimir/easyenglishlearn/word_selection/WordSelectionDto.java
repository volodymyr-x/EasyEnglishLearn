package com.example.vladimir.easyenglishlearn.word_selection;

import com.example.vladimir.easyenglishlearn.Constants.Exercises;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;

class WordSelectionDto {

    private boolean mTranslationDirection;
    private ArrayList<Word> mSelectedWordList;
    @Exercises
    private String mExercise;

    WordSelectionDto(boolean translationDirection,
                     ArrayList<Word> selectedWordList,
                     @Exercises String exercise) {
        mTranslationDirection = translationDirection;
        mSelectedWordList = selectedWordList;
        mExercise = exercise;
    }

    boolean isTranslationDirection() {
        return mTranslationDirection;
    }

    ArrayList<Word> getSelectedWordList() {
        return mSelectedWordList;
    }

    String getExercise() {
        return mExercise;
    }
}
