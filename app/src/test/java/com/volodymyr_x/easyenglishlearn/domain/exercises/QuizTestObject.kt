package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizStageState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

val wordList = listOf(
    WordUI(id = 1, lexeme = "Apple", translation = "Яблуко"),
    WordUI(id = 2, lexeme = "Pineapple", translation = "Ананас"),
    WordUI(id = 3, lexeme = "Banana", translation = "Банан"),
)

val startStageStateLexemeToTranslation = QuizStageState(
    iteration = 1,
    errorCount = 0,
    question = wordList[0].lexeme,
    isLexemeToTranslation = true,
    currentWord = wordList[0],
    wordList = wordList,
    answers = listOf(
        wordList[0].translation,
        wordList[1].translation,
        wordList[2].translation,
    )
)

val secondStageStateLexemeToTranslation = QuizStageState(
    iteration = 2,
    errorCount = 0,
    question = wordList[1].lexeme,
    isLexemeToTranslation = true,
    currentWord = wordList[1],
    wordList = wordList,
    answers = listOf(
        wordList[0].translation,
        wordList[1].translation,
        wordList[2].translation,
    )
)

val lastStageStateLexemeToTranslation = QuizStageState(
    iteration = 3,
    errorCount = 0,
    question = wordList[2].lexeme,
    isLexemeToTranslation = true,
    currentWord = wordList[2],
    wordList = wordList,
    answers = listOf(
        wordList[0].translation,
        wordList[1].translation,
        wordList[2].translation,
    )
)

val startStageStateTranslationToLexeme = QuizStageState(
    iteration = 1,
    errorCount = 0,
    question = wordList[0].translation,
    isLexemeToTranslation = false,
    currentWord = wordList[0],
    wordList = wordList,
    answers = listOf(
        wordList[0].lexeme,
        wordList[1].lexeme,
        wordList[2].lexeme,
    )
)

val secondStageStateTranslationToLexeme = QuizStageState(
    iteration = 2,
    errorCount = 0,
    question = wordList[1].translation,
    isLexemeToTranslation = false,
    currentWord = wordList[1],
    wordList = wordList,
    answers = listOf(
        wordList[0].lexeme,
        wordList[1].lexeme,
        wordList[2].lexeme,
    )
)

val lastStageStateTranslationToLexeme = QuizStageState(
    iteration = 3,
    errorCount = 0,
    question = wordList[2].translation,
    isLexemeToTranslation = false,
    currentWord = wordList[2],
    wordList = wordList,
    answers = listOf(
        wordList[0].lexeme,
        wordList[1].lexeme,
        wordList[2].lexeme,
    )
)
