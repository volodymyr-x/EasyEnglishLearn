package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.ui.exercises.quiz.QuizState
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CheckQuizAnswerUseCaseTest {

    val useCase = CheckQuizAnswerUseCase()

    @Test
    fun `first stage state lexeme to translation`() {
        val result = runBlocking {
            useCase(
                currentExerciseState = QuizState.LoadingState,
                wordList = wordList,
                isLexemeToTranslation = true
            )
        }
        val expectedResult = startStageStateLexemeToTranslation
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.translation))
    }

    @Test
    fun `non last correct user answer lexeme to translation`() {
        val result = runBlocking {
            useCase(
                userAnswer = startStageStateLexemeToTranslation.currentWord?.translation ?: "",
                currentExerciseState = QuizState.StageState(startStageStateLexemeToTranslation)
            )
        }
        val expectedResult = secondStageStateLexemeToTranslation
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.translation))
        assert(stageState.incorrectAnswer.isEmpty())
    }

    @Test
    fun `incorrect user answer lexeme to translation`() {
        val result = runBlocking {
            useCase(
                userAnswer = "Apple111",
                currentExerciseState = QuizState.StageState(startStageStateLexemeToTranslation)
            )
        }
        val expectedResult = startStageStateLexemeToTranslation.copy(
            errorCount = startStageStateLexemeToTranslation.errorCount + 1
        )
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.incorrectAnswer.isNotEmpty())
    }

    @Test
    fun `last correct user answer lexeme to translation`() {
        val result = runBlocking {
            useCase(
                userAnswer = lastStageStateLexemeToTranslation.currentWord?.translation ?: "",
                currentExerciseState = QuizState.StageState(lastStageStateLexemeToTranslation)
            )
        }
        assert(result is QuizState.CompletedState)
    }

    @Test
    fun `first stage state translation to lexeme`() {
        val result = runBlocking {
            useCase(
                currentExerciseState = QuizState.LoadingState,
                wordList = wordList,
                isLexemeToTranslation = false
            )
        }
        val expectedResult = startStageStateTranslationToLexeme
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.lexeme))
    }

    @Test
    fun `non last correct user answer translation to lexeme`() {
        val result = runBlocking {
            useCase(
                userAnswer = startStageStateTranslationToLexeme.currentWord?.lexeme ?: "",
                currentExerciseState = QuizState.StageState(startStageStateTranslationToLexeme)
            )
        }
        val expectedResult = secondStageStateTranslationToLexeme
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.lexeme))
        assert(stageState.incorrectAnswer.isEmpty())
    }

    @Test
    fun `incorrect user answer translation to lexeme`() {
        val result = runBlocking {
            useCase(
                userAnswer = "Apple111",
                currentExerciseState = QuizState.StageState(startStageStateTranslationToLexeme)
            )
        }
        val expectedResult = startStageStateTranslationToLexeme.copy(
            errorCount = startStageStateTranslationToLexeme.errorCount + 1
        )
        assert(result is QuizState.StageState)
        val stageState = (result as QuizState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.incorrectAnswer.isNotEmpty())
    }

    @Test
    fun `last correct user answer translation to lexeme`() {
        val result = runBlocking {
            useCase(
                userAnswer = lastStageStateTranslationToLexeme.currentWord?.lexeme ?: "",
                currentExerciseState = QuizState.StageState(lastStageStateTranslationToLexeme)
            )
        }
        assert(result is QuizState.CompletedState)
    }
}
