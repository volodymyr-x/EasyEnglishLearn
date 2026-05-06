package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.model.Answer
import com.volodymyr_x.easyenglishlearn.ui.exercises.ExerciseState
import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizStageState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

class CheckQuizAnswerUseCase {

    operator fun invoke(
        currentExerciseState: ExerciseState,
        userAnswer: String = "",
        isLexemeToTranslation: Boolean = true,
        wordList: List<WordUI> = emptyList()
    ): ExerciseState {
        return when (currentExerciseState) {
            is ExerciseState.LoadingState -> getInitialQuizState(wordList, isLexemeToTranslation)
            is ExerciseState.CompletedState -> currentExerciseState
            is ExerciseState.StageState -> checkAnswer(userAnswer, currentExerciseState.data)
        }
    }

    private fun getInitialQuizState(
        wordList: List<WordUI>,
        isLexemeToTranslation: Boolean
    ): ExerciseState {
        val initialWord = wordList[0]
        val initialQuizState = QuizStageState(
            iteration = 0,
            errorCount = 0,
            isLexemeToTranslation = isLexemeToTranslation,
            currentWord = initialWord,
            wordList = wordList,
        )
        return ExerciseState.StageState(
            initialQuizState.copy(
                iteration = 1,
                question = createQuestion(initialQuizState),
                answers = createAnswers(
                    wordList,
                    initialWord,
                    isLexemeToTranslation
                )
            )
        )
    }

    private fun checkAnswer(userAnswer: String, stageState: QuizStageState): ExerciseState {
        val answer = Answer(
            question = stageState.currentWord,
            answer = userAnswer,
            isLexemeToTranslation = stageState.isLexemeToTranslation
        )
        if (answer.isCorrect) {
            val isLastIteration = stageState.iteration == stageState.wordList.size
            return if (isLastIteration) {
                ExerciseState.CompletedState(stageState)
            } else {
                val newCurrentWord = stageState.wordList[stageState.iteration]
                ExerciseState.StageState(
                    stageState.copy(
                        iteration = stageState.iteration + 1,
                        currentWord = newCurrentWord,
                        question = createQuestion(stageState),
                        answers = createAnswers(
                            stageState.wordList,
                            newCurrentWord,
                            stageState.isLexemeToTranslation
                        )
                    )
                )
            }
        } else {
            return ExerciseState.StageState(
                stageState.copy(
                    errorCount = stageState.errorCount + 1
                )
            )
        }
    }

    private fun createAnswers(
        wordList: List<WordUI>,
        currentWord: WordUI,
        isLexemeToTranslation: Boolean
    ): List<String> {
        val answerList = wordList
            .filter { it != currentWord }
            .shuffled()
            .take(2)
            .toMutableList()
            .also { list ->
                list.add(currentWord)
            }
        return answerList
            .map { convertWordToAnswer(it, isLexemeToTranslation) }
            .shuffled()
    }


    private fun createQuestion(stageState: QuizStageState): String =
        if (stageState.isLexemeToTranslation) {
            stageState.wordList[stageState.iteration].lexeme
        } else {
            stageState.wordList[stageState.iteration].translation
        }

    private fun convertWordToAnswer(word: WordUI, translationDirection: Boolean): String =
        if (translationDirection) word.translation else word.lexeme
}
