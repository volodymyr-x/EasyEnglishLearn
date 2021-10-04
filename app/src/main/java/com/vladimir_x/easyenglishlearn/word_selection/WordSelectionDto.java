package com.vladimir_x.easyenglishlearn.word_selection;

import com.vladimir_x.easyenglishlearn.Constants.Exercises;
import com.vladimir_x.easyenglishlearn.model.Word;

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
