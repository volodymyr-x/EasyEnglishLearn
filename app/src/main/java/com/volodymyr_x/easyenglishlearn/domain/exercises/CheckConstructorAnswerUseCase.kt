package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.model.Answer
import com.volodymyr_x.easyenglishlearn.ui.exercises.constructor.ConstructorStageState
import com.volodymyr_x.easyenglishlearn.ui.exercises.constructor.ConstructorState
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CheckConstructorAnswerUseCase(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend operator fun invoke(
        currentExerciseState: ConstructorState,
        userAnswer: String = "",
        isLexemeToTranslation: Boolean = true,
        wordList: List<WordUI> = emptyList()
    ): ConstructorState = withContext(dispatcher) {
        when (currentExerciseState) {
            is ConstructorState.LoadingState -> getInitialExerciseState(
                wordList,
                isLexemeToTranslation
            )
            is ConstructorState.CompletedState -> currentExerciseState
            is ConstructorState.StageState -> checkAnswer(
                userAnswer,
                currentExerciseState.data
            )
            is ConstructorState.UndoStageState -> getUndoStageStage(currentExerciseState.data)
        }
    }

    private fun getUndoStageStage(stageState: ConstructorStageState): ConstructorState {
        val undoAnswer = stageState.currentAnswer.lastOrNull()?.toString() ?: ""
        val newLetters = if (undoAnswer.isNotEmpty()) {
            stageState.letters + undoAnswer
        } else {
            stageState.letters
        }
        return ConstructorState.StageState(
            stageState.copy(
                letters = newLetters,
                currentAnswer = stageState.currentAnswer.dropLast(1)
            )
        )
    }

    private fun getUpdatedStageStage(
        userAnswer: String,
        stageState: ConstructorStageState
    ): ConstructorStageState {
        return stageState.copy(
            letters = stageState.letters - userAnswer,
            currentAnswer = stageState.currentAnswer + userAnswer,
            incorrectAnswer = ""
        )
    }

    private fun checkAnswer(
        userAnswer: String,
        stageState: ConstructorStageState
    ): ConstructorState {
        val newState = getUpdatedStageStage(userAnswer, stageState)
        val answer = Answer(
            question = stageState.currentWord,
            answer = newState.currentAnswer,
            isLexemeToTranslation = stageState.isLexemeToTranslation
        )
        return when {
            newState.letters.isNotEmpty() -> ConstructorState.StageState(newState)
            answer.isCorrect -> getNextStageState(newState)
            else -> getErrorStageState(newState)
        }
    }

    private fun getErrorStageState(stageState: ConstructorStageState): ConstructorState.StageState =
        ConstructorState.StageState(
            stageState.copy(
                errorCount = stageState.errorCount + 1,
                incorrectAnswer = stageState.currentAnswer,
                currentAnswer = "",
                letters = createAnswers(
                    stageState.currentWord ?: throw IllegalStateException("Current word is null"),
                    stageState.isLexemeToTranslation
                ),
            )
        )

    private fun getNextStageState(stageState: ConstructorStageState): ConstructorState {
        val isLastIteration = stageState.iteration == stageState.wordList.size
        return if (isLastIteration) {
            ConstructorState.CompletedState(stageState)
        } else {
            val newCurrentWord = stageState.wordList[stageState.iteration]
            ConstructorState.StageState(
                stageState.copy(
                    iteration = stageState.iteration + 1,
                    currentWord = newCurrentWord,
                    question = if (stageState.isLexemeToTranslation) newCurrentWord.lexeme else newCurrentWord.translation,
                    letters = createAnswers(newCurrentWord, stageState.isLexemeToTranslation),
                    currentAnswer = "",
                    incorrectAnswer = ""
                )
            )
        }
    }

    private fun getInitialExerciseState(
        questionList: List<WordUI>,
        isLexemeToTranslation: Boolean
    ): ConstructorState {
        return ConstructorState.StageState(
            ConstructorStageState(
                question = createQuestion(questionList, isLexemeToTranslation),
                letters = createAnswers(questionList[0], isLexemeToTranslation),
                isLexemeToTranslation = isLexemeToTranslation,
                currentWord = questionList[0],
                wordList = questionList
            )
        )
    }

    private fun createAnswers(
        currentWord: WordUI,
        isLexemeToTranslation: Boolean
    ): List<String> {
        val answer = convertWordToAnswer(currentWord, isLexemeToTranslation)
        return answer
            .map { it.toString() }
            .shuffled()
    }

    private fun createQuestion(
        questionList: List<WordUI>,
        isLexemeToTranslation: Boolean
    ): String =
        if (isLexemeToTranslation) {
            questionList[0].lexeme
        } else {
            questionList[0].translation
        }

    private fun convertWordToAnswer(word: WordUI, translationDirection: Boolean): String =
        if (translationDirection) word.translation else word.lexeme

}
