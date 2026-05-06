package com.volodymyr_x.easyenglishlearn.domain.exercises

import com.volodymyr_x.easyenglishlearn.ui.exercises.ExerciseState
import org.junit.Test

class CheckQuizAnswerUseCaseTest {

    val useCase = CheckQuizAnswerUseCase()

    @Test
    fun `first stage state lexeme to translation`() {
        val result = useCase(
            currentExerciseState = ExerciseState.LoadingState,
            wordList = wordList,
            isLexemeToTranslation = true
        )
        val expectedResult = startStageStateLexemeToTranslation
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.translation))
    }

    @Test
    fun `non last correct user answer lexeme to translation`() {
        val result = useCase(
            userAnswer = startStageStateLexemeToTranslation.currentWord?.translation ?: "",
            currentExerciseState = ExerciseState.StageState(startStageStateLexemeToTranslation)
        )
        val expectedResult = secondStageStateLexemeToTranslation
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.translation))
    }

    @Test
    fun `incorrect user answer lexeme to translation`() {
        val result = useCase(
            userAnswer = "Apple111",
            currentExerciseState = ExerciseState.StageState(startStageStateLexemeToTranslation)
        )
        val expectedResult = startStageStateLexemeToTranslation.copy(
            errorCount = startStageStateLexemeToTranslation.errorCount + 1
        )
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
    }

    @Test
    fun `last correct user answer lexeme to translation`() {
        val result = useCase(
            userAnswer = lastStageStateLexemeToTranslation.currentWord?.translation ?: "",
            currentExerciseState = ExerciseState.StageState(lastStageStateLexemeToTranslation)
        )
        assert(result is ExerciseState.CompletedState)
    }

    @Test
    fun `first stage state translation to lexeme`() {
        val result = useCase(
            currentExerciseState = ExerciseState.LoadingState,
            wordList = wordList,
            isLexemeToTranslation = false
        )
        val expectedResult = startStageStateTranslationToLexeme
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.lexeme))
    }

    @Test
    fun `non last correct user answer translation to lexeme`() {
        val result = useCase(
            userAnswer = startStageStateTranslationToLexeme.currentWord?.lexeme ?: "",
            currentExerciseState = ExerciseState.StageState(startStageStateTranslationToLexeme)
        )
        val expectedResult = secondStageStateTranslationToLexeme
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
        assert(stageState.answers.contains(stageState.currentWord?.lexeme))
    }

    @Test
    fun `incorrect user answer translation to lexeme`() {
        val result = useCase(
            userAnswer = "Apple111",
            currentExerciseState = ExerciseState.StageState(startStageStateTranslationToLexeme)
        )
        val expectedResult = startStageStateTranslationToLexeme.copy(
            errorCount = startStageStateTranslationToLexeme.errorCount + 1
        )
        assert(result is ExerciseState.StageState)
        val stageState = (result as ExerciseState.StageState).data
        assert(stageState.iteration == expectedResult.iteration)
        assert(stageState.errorCount == expectedResult.errorCount)
        assert(stageState.currentWord == expectedResult.currentWord)
    }

    @Test
    fun `last correct user answer translation to lexeme`() {
        val result = useCase(
            userAnswer = lastStageStateTranslationToLexeme.currentWord?.lexeme ?: "",
            currentExerciseState = ExerciseState.StageState(lastStageStateTranslationToLexeme)
        )
        assert(result is ExerciseState.CompletedState)
    }
}
