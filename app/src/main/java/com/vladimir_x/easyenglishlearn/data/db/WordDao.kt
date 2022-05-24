package com.vladimir_x.easyenglishlearn.data.db

import androidx.room.Dao
import com.vladimir_x.easyenglishlearn.model.Word
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class WordDao {
    @Query("SELECT * FROM word WHERE category = :categoryName")
    abstract suspend fun getWordsByCategory(categoryName: String): List<Word>

    @Query("SELECT DISTINCT category FROM word")
    abstract fun getAllCategories(): LiveData<List<String>>

    @Transaction
    open suspend fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        removeCategory(oldCategoryName)
        setCategory(wordList, newCategoryName)
        insertNewCategory(wordList)
    }

    @Query("DELETE from word WHERE category = :categoryName")
    abstract suspend fun removeCategory(categoryName: String)

    @Transaction
    open suspend fun addNewCategory(wordList: List<Word>, newCategoryName: String) {
        setCategory(wordList, newCategoryName)
        insertNewCategory(wordList)
    }

    @Insert
    abstract suspend fun insertNewCategory(wordList: List<Word>)

    private fun setCategory(wordList: List<Word>, newCategoryName: String) {
        for (word in wordList) {
            word.category = newCategoryName
        }
    }
}