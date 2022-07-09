package com.vladimir_x.easyenglishlearn.data.db

import androidx.room.Dao
import com.vladimir_x.easyenglishlearn.model.Word
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WordDao {
    @Query("SELECT * FROM word WHERE category = :categoryName")
    abstract suspend fun getWordsByCategory(categoryName: String): List<Word>

    @Query("SELECT DISTINCT category FROM word")
    abstract fun getAllCategories(): Flow<List<String>>

    @Transaction
    open suspend fun updateCategory(
        oldCategoryName: String,
        wordList: List<Word>
    ) {
        removeCategory(oldCategoryName)
        insertNewCategory(wordList)
    }

    @Query("DELETE from word WHERE category = :categoryName")
    abstract suspend fun removeCategory(categoryName: String)

    @Insert
    abstract suspend fun insertNewCategory(wordList: List<Word>)
}