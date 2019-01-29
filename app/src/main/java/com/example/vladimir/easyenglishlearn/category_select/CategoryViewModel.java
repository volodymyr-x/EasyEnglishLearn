package com.example.vladimir.easyenglishlearn.category_select;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;

import java.util.List;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository mRepository;
    private MutableLiveData<List<String>> mCategoriesLiveData;
    private SingleLiveEvent<String> mEditCategoryLiveData;
    private SingleLiveEvent<String> mRemoveDialogLiveData;
    private SingleLiveEvent<String> mOpenCategoryLiveData;
    private SingleLiveEvent<Void> mRemoveCategoryLiveData;


    public CategoryViewModel() {
        mRepository = CategoryRepositoryImpl.getInstance();
        mCategoriesLiveData = mRepository.getAllCategories();
        mEditCategoryLiveData = new SingleLiveEvent<>();
        mRemoveDialogLiveData = new SingleLiveEvent<>();
        mOpenCategoryLiveData = new SingleLiveEvent<>();
        mRemoveCategoryLiveData = new SingleLiveEvent<>();
    }

    public void onFabClick() {
        mEditCategoryLiveData.setValue(EMPTY_STRING);
    }

    public void onRemoveIconClick(String categoryName) {
        mRemoveDialogLiveData.setValue(categoryName);
    }

    public void onEditIconClick(String categoryName) {
        mEditCategoryLiveData.setValue(categoryName);
    }

    public void onRvItemClick(String categoryName) {
        mOpenCategoryLiveData.setValue(categoryName);
    }

    LiveData<String> getOpenCategoryLiveData() {
        return mOpenCategoryLiveData;
    }

    LiveData<String> getEditCategoryLiveData() {
        return mEditCategoryLiveData;
    }

    LiveData<String> getRemoveDialogLiveData() {
        return mRemoveDialogLiveData;
    }

    LiveData<Void> getRemoveCategoryLiveData() {
        return mRemoveCategoryLiveData;
    }

    LiveData<List<String>> getCategoriesLiveData() {
        return mCategoriesLiveData;
    }

    public void removeCategory(String categoryName) {
        mRepository.removeCategory(categoryName);
        mRemoveCategoryLiveData.call();
    }

    public void cancelRemoving() {
        mRemoveCategoryLiveData.call();
    }
}
