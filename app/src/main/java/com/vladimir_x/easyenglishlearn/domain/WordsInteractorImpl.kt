package com.vladimir_x.easyenglishlearn.domain

import androidx.lifecycle.LiveData
import com.vladimir_x.easyenglishlearn.domain.repository.WordsRepository
import com.vladimir_x.easyenglishlearn.model.Word

class WordsInteractorImpl(private val wordsRepository: WordsRepository) : WordsInteractor {
    override suspend fun getWordsByCategory(categoryName: String): List<Word> =
        wordsRepository.getWordsByCategory(categoryName)

    override fun getAllCategories(): LiveData<List<String>> =
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