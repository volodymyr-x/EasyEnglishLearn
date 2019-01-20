package com.example.vladimir.easyenglishlearn.category_select;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;

class CategoryViewModel extends ViewModel {

    private CategoryRepository mRepository;
    private MutableLiveData<List<String>> mLiveData;


    CategoryViewModel() {
        mRepository = CategoryRepositoryImpl.getInstance();
        mLiveData = mRepository.getAllCategories();
    }

    LiveData<List<String>> getCategories() {
        return mLiveData;
    }

    void addNewCategory(String categoryName, List<Word> wordList) {
        mRepository.addNewCategory(categoryName, wordList);
    }

    void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        mRepository.updateCategory(oldCategoryName, newCategoryName, wordList);
    }

    void removeCategory(String categoryName) {
        mRepository.removeCategory(categoryName);
    }
}
