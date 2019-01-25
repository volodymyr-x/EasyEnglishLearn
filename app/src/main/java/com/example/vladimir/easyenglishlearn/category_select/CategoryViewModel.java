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
    private MutableLiveData<List<String>> mLiveData;
    private SingleLiveEvent<String> mEditCategory;
    private SingleLiveEvent<String> mRemoveDialog;
    private SingleLiveEvent<String> mOpenCategory;
    private SingleLiveEvent<Void> mRemoveCategory;


    public CategoryViewModel() {
        mRepository = CategoryRepositoryImpl.getInstance();
        mLiveData = mRepository.getAllCategories();
        mEditCategory = new SingleLiveEvent<>();
        mRemoveDialog = new SingleLiveEvent<>();
        mOpenCategory = new SingleLiveEvent<>();
        mRemoveCategory = new SingleLiveEvent<>();
    }

    public void onFabClick() {
        mEditCategory.setValue(EMPTY_STRING);
    }

    public void onRemoveIconClick(String categoryName) {
        mRemoveDialog.setValue(categoryName);
    }

    public void onEditIconClick(String categoryName) {
        mEditCategory.setValue(categoryName);
    }

    public void onRvItemClick(String categoryName) {
        mOpenCategory.setValue(categoryName);
    }

    LiveData<String> getOpenCategory() {
        return mOpenCategory;
    }

    LiveData<String> getEditCategory() {
        return mEditCategory;
    }

    LiveData<String> getRemoveDialog() {
        return mRemoveDialog;
    }

    LiveData<Void> getRemoveCategory() {
        return mRemoveCategory;
    }

    LiveData<List<String>> getCategoryList() {
        return mLiveData;
    }

    public void removeCategory(String categoryName) {
        mRepository.removeCategory(categoryName);
        mRemoveCategory.call();
    }

    public void cancelRemoving() {
        mRemoveCategory.call();
    }
}
