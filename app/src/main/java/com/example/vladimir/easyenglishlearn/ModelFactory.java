package com.example.vladimir.easyenglishlearn;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.vladimir.easyenglishlearn.category_edit.CategoryEditViewModel;
import com.example.vladimir.easyenglishlearn.word_selection.WordSelectionViewModel;

import java.util.HashMap;
import java.util.Map;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String mCategoryName;
    private ViewModel mViewModel;
    private static Map<String, ModelFactory> sFactoriesMap = new HashMap<>();


    private ModelFactory(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public static ModelFactory getInstance(String categoryName) {
        if (!sFactoriesMap.containsKey(categoryName)) {
            sFactoriesMap.put(categoryName, new ModelFactory(categoryName));
        }
        return sFactoriesMap.get(categoryName);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CategoryEditViewModel.class) {
            if (mViewModel == null || mViewModel.getClass() != CategoryEditViewModel.class) {
                mViewModel = new CategoryEditViewModel(mCategoryName);
            }
            return (T) mViewModel;
        } else if (modelClass == WordSelectionViewModel.class) {
            if (mViewModel == null || mViewModel.getClass() != WordSelectionViewModel.class) {
                mViewModel = new WordSelectionViewModel(mCategoryName);
            }
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
