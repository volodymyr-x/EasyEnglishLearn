package com.example.vladimir.easyenglishlearn.category_edit;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final String mCategoryName;

    public ModelFactory(String categoryName) {
        this.mCategoryName = categoryName;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CategoryEditViewModel.class) {
            return (T) new CategoryEditViewModel(mCategoryName);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
