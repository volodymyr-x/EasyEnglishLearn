package com.example.vladimir.easyenglishlearn;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.vladimir.easyenglishlearn.category_edit.CategoryEditViewModel;
import com.example.vladimir.easyenglishlearn.exercises.WordQuizViewModel;
import com.example.vladimir.easyenglishlearn.model.Word;
import com.example.vladimir.easyenglishlearn.word_selection.WordSelectionViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private String mCategoryName;
    private List<Word> mWordList;
    private boolean mTranslationDirection;
    private ViewModel mViewModel;
    private static Map<String, ModelFactory> sFactoriesMap = new HashMap<>();

    private ModelFactory(String categoryName) {
        this.mCategoryName = categoryName;
    }

    private ModelFactory(List<Word> wordList, boolean translationDirection) {
        this(EMPTY_STRING);
        mWordList = wordList;
        mTranslationDirection = translationDirection;
    }

    public static ModelFactory getInstance(String categoryName) {
        if (!sFactoriesMap.containsKey(categoryName)) {
            sFactoriesMap.put(categoryName, new ModelFactory(categoryName));
        }
        return sFactoriesMap.get(categoryName);
    }

    public static ModelFactory getInstance(List<Word> wordList, boolean translationDirection) {
        if (!sFactoriesMap.containsKey(EMPTY_STRING)) {
            sFactoriesMap.put(EMPTY_STRING, new ModelFactory(wordList, translationDirection));
        }
        return sFactoriesMap.get(EMPTY_STRING);
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
        } else if (modelClass == WordQuizViewModel.class) {
            if (mViewModel == null || mViewModel.getClass() != WordQuizViewModel.class) {
                mViewModel = new WordQuizViewModel(mWordList, mTranslationDirection);
            }
            return (T) mViewModel;
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
