package com.vladimir_x.easyenglishlearn;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface Constants {

    String CATEGORY_NAME = "CATEGORY_NAME";
    String DIALOG_REMOVE_CATEGORY = "DIALOG_REMOVE_CATEGORY";
    String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";
    String TRANSLATION_DIRECTION = "TRANSLATION_DIRECTION";
    String EXERCISE_TYPE = "EXERCISE_TYPE";
    String EXERCISE_CHOICE_FRAGMENT = "EXERCISE_CHOICE_FRAGMENT";
    String SELECTED_WORDS = "SELECTED_WORDS";
    String EMPTY_STRING = "";
    String DATABASE_NAME = "english_learn";
    int ANSWERS_COUNT = 3;
    int ACTION_OPEN_CATEGORY = 100;
    int ACTION_EDIT_CATEGORY = 101;
    int ACTION_ABOUT = 102;

    @Retention(SOURCE)
    @StringDef({ WORD_CONSTRUCTOR, WORD_QUIZ })
    @interface Exercises {}
    String WORD_CONSTRUCTOR = "WORD_CONSTRUCTOR";
    String WORD_QUIZ = "WORD_QUIZ";
}
