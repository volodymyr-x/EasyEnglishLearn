package com.example.vladimir.easyenglishlearn.category_edit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.List;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class CategoryEditViewModel extends ViewModel {

    public final ObservableField<String> categoryName = new ObservableField<>();
    public final ObservableField<String> lexeme = new ObservableField<>();
    public final ObservableField<String> translation = new ObservableField<>();
    private SingleLiveEvent<Integer> mToastMessage;
    private SingleLiveEvent<Void> mFragmentClose;
    private MutableLiveData<List<Word>> mLiveData;
    private CategoryRepository mRepository;
    private String mOldCategoryName;
    private int mWordIndex;


    CategoryEditViewModel(String categoryName) {
        mRepository = CategoryRepositoryImpl.getInstance();
        mOldCategoryName = categoryName;
        this.categoryName.set(categoryName);
        mLiveData = new MutableLiveData<>();
        mLiveData.setValue(mRepository.getWordsByCategory(categoryName));
        mToastMessage = new SingleLiveEvent<>();
        mFragmentClose = new SingleLiveEvent<>();
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onBtnSaveCategoryClick() {
        String newCategoryName = categoryName.get().trim();
        if (TextUtils.isEmpty(newCategoryName)) {
            showToast(R.string.cef_toast_save_edit_category);
        } else {
            if (TextUtils.isEmpty(mOldCategoryName)) {
                addNewCategory(newCategoryName, mLiveData.getValue());
            } else {
                updateCategory(mOldCategoryName, newCategoryName, mLiveData.getValue());
            }
            mFragmentClose.call();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onBtnSaveWordClick() {
        if (isTextFieldsNotEmpty()) {
            Word newWord = new Word(lexeme.get().trim(), translation.get().trim());
            List<Word> wordList = mLiveData.getValue();
            if (mWordIndex >= 0) {
                wordList.set(mWordIndex, newWord);
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
        mWordIndex = mLiveData.getValue().indexOf(word);
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isTextFieldsNotEmpty() {
        return !TextUtils.isEmpty(categoryName.get().trim()) &&
                !TextUtils.isEmpty(lexeme.get().trim()) &&
                !TextUtils.isEmpty(translation.get().trim());
    }

    private void cleanTextFields() {
        lexeme.set(EMPTY_STRING);
        translation.set(EMPTY_STRING);
        mWordIndex = -1;
    }

    private void showToast(@StringRes int resId) {
        mToastMessage.setValue(resId);
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

    LiveData<Integer> getToastMessage() {
        return mToastMessage;
    }

    LiveData<Void> getFragmentClose() {
        return mFragmentClose;
    }
}
