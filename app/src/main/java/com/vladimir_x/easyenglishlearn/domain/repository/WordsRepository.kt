package com.vladimir_x.easyenglishlearn.domain.repository

import com.vladimir_x.easyenglishlearn.model.Word
import kotlinx.coroutines.flow.Flow

interface WordsRepository {
    suspend fun getWordsByCategory(categoryName: String): List<Word>

    fun getAllCategories(): Flow<List<String>>

    suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    )

    suspend fun removeCategory(categoryName: String)

    suspend fun addNewCategory(wordList: List<Word>, newCategoryName: String)

    suspend fun insertNewCategory(wordList: List<Word>)
}