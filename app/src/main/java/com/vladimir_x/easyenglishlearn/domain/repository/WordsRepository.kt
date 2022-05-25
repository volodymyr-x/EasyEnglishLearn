package com.vladimir_x.easyenglishlearn.domain.repository

import androidx.lifecycle.LiveData
import com.vladimir_x.easyenglishlearn.model.Word

interface WordsRepository {
    suspend fun getWordsByCategory(categoryName: String): List<Word>

    fun getAllCategories(): LiveData<List<String>>

    suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    )

    suspend fun removeCategory(categoryName: String)

    suspend fun addNewCategory(wordList: List<Word>, newCategoryName: String)

    suspend fun insertNewCategory(wordList: List<Word>)
}