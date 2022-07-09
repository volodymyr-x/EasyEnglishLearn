package com.vladimir_x.easyenglishlearn.domain

import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import kotlinx.coroutines.flow.Flow

interface WordsInteractor {
    suspend fun getWordsByCategory(categoryName: String): List<WordUI>

    fun getAllCategories(): Flow<List<String>>

    suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordUIList: List<WordUI>
    )

    suspend fun removeCategory(categoryName: String)

    suspend fun addNewCategory(wordUIList: List<WordUI>, newCategoryName: String)
}
