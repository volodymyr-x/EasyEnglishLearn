package com.vladimir_x.easyenglishlearn.data.repository

import com.vladimir_x.easyenglishlearn.data.db.WordDao
import com.vladimir_x.easyenglishlearn.domain.repository.WordsRepository
import com.vladimir_x.easyenglishlearn.model.Word
import kotlinx.coroutines.flow.Flow

class WordsRepositoryImpl(private val wordDao: WordDao) : WordsRepository {
    override suspend fun getWordsByCategory(categoryName: String): List<Word> =
        wordDao.getWordsByCategory(categoryName)

    override fun getAllCategories(): Flow<List<String>> = wordDao.getAllCategories()

    override suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        wordDao.updateCategory(oldCategoryName, newCategoryName, wordList)
    }

    override suspend fun removeCategory(categoryName: String) {
        wordDao.removeCategory(categoryName)
    }

    override suspend fun addNewCategory(wordList: List<Word>, newCategoryName: String) {
        wordDao.addNewCategory(wordList, newCategoryName)
    }

    override suspend fun insertNewCategory(wordList: List<Word>) {
        wordDao.insertNewCategory(wordList)
    }

}