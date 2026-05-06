package com.volodymyr_x.easyenglishlearn.di.modules

import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractorImpl
import com.volodymyr_x.easyenglishlearn.domain.exercises.CheckQuizAnswerUseCase
import com.volodymyr_x.easyenglishlearn.domain.repository.WordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideWordsInteractor(wordsRepository: WordsRepository) : WordsInteractor {
        return WordsInteractorImpl(wordsRepository)
    }

    @Provides
    fun provideCheckQuizAnswerUseCase() : CheckQuizAnswerUseCase {
        return CheckQuizAnswerUseCase()
    }
}
