package com.example.vladimir.easyenglishlearn.db;

import android.arch.lifecycle.MutableLiveData;

import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;

public interface CategoryRepository {

    List<Word> getWordsByCategory(String categoryName);

    //List<String> getAllCategories();

    MutableLiveData<List<String>> getAllCategories();

    void addNewCategory(String categoryName, List<Word> wordList);

    void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList);

    void removeCategory(String categoryName);

    //void setDataChangeListener(String key, DataChangeListener listener);

    //void removeDataChangeListener(String key);

//    interface DataChangeListener {
//        void dataIsChanged();
//    }
}
