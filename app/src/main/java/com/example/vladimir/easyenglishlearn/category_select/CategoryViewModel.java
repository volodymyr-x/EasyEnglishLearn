package com.example.vladimir.easyenglishlearn.category_select;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository mRepository;
    private MutableLiveData<List<String>> mLiveData;


    public CategoryViewModel() {
        mRepository = CategoryRepositoryImpl.getInstance();
        mLiveData = mRepository.getAllCategories();
    }

    LiveData<List<String>> getCategoryList() {
        return mLiveData;
    }

    void removeCategory(String categoryName) {
        mRepository.removeCategory(categoryName);
    }
}
