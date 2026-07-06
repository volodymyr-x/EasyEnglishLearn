package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.model.Answer
import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizState
import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizStageState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckQuizAnswerUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(
        currentExerciseState: QuizState,
        userAnswer: String = "",
        isLexemeToTranslation: Boolean = true,
        wordList: List<WordUI> = emptyList()
    ): QuizState = withContext(dispatcher) {
        when (currentExerciseState) {
            is QuizState.LoadingState -> getInitialQuizState(wordList, isLexemeToTranslation)
            is QuizState.CompletedState -> currentExerciseState
            is QuizState.StageState -> checkAnswer(userAnswer, currentExerciseState.data)
        }
    }

    private fun getInitialQuizState(
        wordList: List<WordUI>,
        isLexemeToTranslation: Boolean
    ): QuizState {
        val initialWord = wordList[0]
        val initialQuizState = QuizStageState(
            iteration = 0, // maybe should set up as 1
            errorCount = 0,
            isLexemeToTranslation = isLexemeToTranslation,
            currentWord = initialWord,
            wordList = wordList,
        )
        return QuizState.StageState(
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

    private fun checkAnswer(userAnswer: String, stageState: QuizStageState): QuizState {
        val answer = Answer(
            question = stageState.currentWord,
            answer = userAnswer,
            isLexemeToTranslation = stageState.isLexemeToTranslation
        )
        if (answer.isCorrect) {
            val isLastIteration = stageState.iteration == stageState.wordList.size
            return if (isLastIteration) {
                QuizState.CompletedState(stageState)
            } else {
                val newCurrentWord = stageState.wordList[stageState.iteration]
                QuizState.StageState(
                    stageState.copy(
                        iteration = stageState.iteration + 1,
                        currentWord = newCurrentWord,
                        question = createQuestion(stageState),
                        answers = createAnswers(
                            stageState.wordList,
                            newCurrentWord,
                            stageState.isLexemeToTranslation,
                        ),
                        incorrectAnswer = ""
                    )
                )
            }
        } else {
            return QuizState.StageState(
                stageState.copy(
                    errorCount = stageState.errorCount + 1,
                    incorrectAnswer = userAnswer
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
            .shuffled() // redundant
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
