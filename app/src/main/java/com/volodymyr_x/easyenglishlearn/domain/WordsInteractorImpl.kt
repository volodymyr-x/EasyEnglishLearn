package com.volodymyr_x.easyenglishlearn.domain

import com.volodymyr_x.easyenglishlearn.domain.repository.WordsRepository
import com.volodymyr_x.easyenglishlearn.model.Word
import kotlinx.coroutines.flow.Flow

class WordsInteractorImpl(private val wordsRepository: WordsRepository) : WordsInteractor {
    override suspend fun getWordsByCategory(categoryName: String): List<Word> =
        wordsRepository.getWordsByCategory(categoryName)

    override fun getAllCategories(): Flow<List<String>> =
        wordsRepository.getAllCategories()

    override suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        wordsRepository.updateCategory(oldCategoryName, newCategoryName, wordList)
    }

    override suspend fun removeCategory(categoryName: String) {
        wordsRepository.removeCategory(categoryName)
    }

    override suspend fun addNewCategory(wordList: List<Word>, newCategoryName: String) {
        wordsRepository.addNewCategory(wordList, newCategoryName)
    }

    override suspend fun insertNewCategory(wordList: List<Word>) {
        wordsRepository.insertNewCategory(wordList)
    }
}
