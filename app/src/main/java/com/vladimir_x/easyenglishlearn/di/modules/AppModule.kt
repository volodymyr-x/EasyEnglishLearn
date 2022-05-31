package com.vladimir_x.easyenglishlearn.di.modules

import android.content.Context
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.data.db.WordDao
import com.vladimir_x.easyenglishlearn.data.repository.WordsRepositoryImpl
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import com.vladimir_x.easyenglishlearn.domain.WordsInteractorImpl
import com.vladimir_x.easyenglishlearn.domain.repository.WordsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideWordsRepository(wordDao: WordDao) : WordsRepository {
        return WordsRepositoryImpl(wordDao)
    }

    @Provides
    fun provideWordsInteractor(wordsRepository: WordsRepository) : WordsInteractor {
        return WordsInteractorImpl(wordsRepository)
    }
}