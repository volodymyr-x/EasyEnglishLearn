package com.example.vladimir.easyenglishlearn.category_edit;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;
import java.util.Objects;

public class CategoryEditViewModel extends AndroidViewModel {

    public final ObservableField<String> categoryName;
    public final ObservableField<String> lexeme;
    public final ObservableField<String> translation;
    private SingleLiveEvent<String> mToastMessage;
    private SingleLiveEvent<Void> mFragmentClose;
    private MutableLiveData<List<Word>> mLiveData;
    private CategoryRepository mRepository;
    private String mOldCategoryName;
    private int wordIndex;


    public CategoryEditViewModel(@NonNull Application application) {
        super(application);
        mLiveData = new MutableLiveData<>();
        categoryName = new ObservableField<>();
        lexeme = new ObservableField<>();
        translation = new ObservableField<>();
        mToastMessage = new SingleLiveEvent<>();
        mFragmentClose = new SingleLiveEvent<>();
        mRepository = CategoryRepositoryImpl.getInstance();
    }

    public void onBtnSaveCategoryClick() {
        String newCategoryName = categoryName.get();
        if (TextUtils.isEmpty(newCategoryName)) {
            showToast(R.string.cef_toast_save_edit_category);
        } else {
            if (mOldCategoryName != null) {
                updateCategory(mOldCategoryName, newCategoryName, mLiveData.getValue());
            } else {
                addNewCategory(newCategoryName, mLiveData.getValue());
            }
            mFragmentClose.call();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onBtnSaveWordClick() {
        if (isTextFieldsNotEmpty()) {
            Word newWord = new Word(Objects.requireNonNull(lexeme.get()).trim(),
                    Objects.requireNonNull(translation.get()).trim());
            List<Word> wordList = mLiveData.getValue();
            if (wordIndex >= 0) {
                wordList.set(wordIndex, newWord);
            } else {
                wordList.add(newWord);
            }
            mLiveData.setValue(wordList);
            cleanTextFields();
        } else {
            showToast(R.string.cef_toast_save_word_empty_fields);
        }
    }

    public void onBtnCleanClick() {
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onIconRemoveWordClick(Word word) {
        List<Word> wordList = mLiveData.getValue();
        wordList.remove(word);
        mLiveData.setValue(wordList);
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onRvItemClick(Word word) {
        lexeme.set(word.getLexeme());
        translation.set(word.getTranslation());
        wordIndex = mLiveData.getValue().indexOf(word);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isTextFieldsNotEmpty() {
        return !TextUtils.isEmpty(categoryName.get().trim()) &&
                !TextUtils.isEmpty(lexeme.get().trim()) &&
                !TextUtils.isEmpty(translation.get().trim());
    }

    private void cleanTextFields() {
        lexeme.set("");
        translation.set("");
        wordIndex = -1;
    }

    private void showToast(@StringRes int resId) {
        String message = getApplication().getString(resId);
        mToastMessage.setValue(message);
    }

    private void init(String categoryName) {
        this.categoryName.set(categoryName);
        mLiveData.setValue(mRepository.getWordsByCategory(categoryName));
        cleanTextFields();
    }

    private void addNewCategory(String categoryName, List<Word> wordList) {
        mRepository.addNewCategory(categoryName, wordList);
    }

    private void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        mRepository.updateCategory(oldCategoryName, newCategoryName, wordList);
    }

    LiveData<List<Word>> getWordList() {
        return mLiveData;
    }

    LiveData<String> getToastMessage() {
        return mToastMessage;
    }

    LiveData<Void> getFragmentClose() {
        return mFragmentClose;
    }

    public void setCategoryName(String oldCategoryName) {
        if (!"".equals(oldCategoryName)) mOldCategoryName = oldCategoryName;
        init(oldCategoryName);
    }
}
