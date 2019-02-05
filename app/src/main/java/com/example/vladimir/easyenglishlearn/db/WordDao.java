package com.example.vladimir.easyenglishlearn.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class WordDao {

    @Query("SELECT * FROM word WHERE category = :categoryName")
    public abstract Single<List<Word>> getWordsByCategory(String categoryName);

    @Query("SELECT DISTINCT category FROM word")
    public abstract LiveData<List<String>> getAllCategories();

    @Transaction
    public void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        removeCategory(oldCategoryName);
        setCategory(wordList, newCategoryName);
        insertNewCategory(wordList);
    }

    @Query("DELETE from word WHERE category = :categoryName")
    public abstract void removeCategory(String categoryName);

    @Transaction
    public void addNewCategory(List<Word> wordList, String newCategoryName) {
        setCategory(wordList, newCategoryName);
        insertNewCategory(wordList);
    }

    @Insert
    abstract void insertNewCategory(List<Word> wordList);

    private void setCategory(List<Word> wordList, String newCategoryName) {
        for (Word word : wordList) {
            word.setCategory(newCategoryName);
        }
    }
}
