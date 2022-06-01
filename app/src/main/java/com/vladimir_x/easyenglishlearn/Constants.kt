package com.vladimir_x.easyenglishlearn

import androidx.annotation.StringDef

interface Constants {
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(WORD_CONSTRUCTOR, WORD_QUIZ)

    annotation class Exercises
    companion object {
        const val CATEGORY_NAME = "CATEGORY_NAME"
        const val DIALOG_REMOVE_CATEGORY = "DIALOG_REMOVE_CATEGORY"
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        const val TRANSLATION_DIRECTION = "TRANSLATION_DIRECTION"
        const val EXERCISE_TYPE = "EXERCISE_TYPE"
        const val EXERCISE_CHOICE_FRAGMENT = "EXERCISE_CHOICE_FRAGMENT"
        const val SELECTED_WORDS = "SELECTED_WORDS"
        const val EMPTY_STRING = ""
        const val DATABASE_NAME = "english_learn"
        const val ANSWERS_COUNT = 3
        const val ACTION_OPEN_CATEGORY = 100
        const val ACTION_EDIT_CATEGORY = 101
        const val ACTION_ABOUT = 102
        const val WORD_CONSTRUCTOR = "WORD_CONSTRUCTOR"
        const val WORD_QUIZ = "WORD_QUIZ"
        const val RESULT_KEY = "RESULT_KEY"
        const val EXERCISE_CHOICE_KEY = "EXERCISE_CHOICE_KEY"
    }
}