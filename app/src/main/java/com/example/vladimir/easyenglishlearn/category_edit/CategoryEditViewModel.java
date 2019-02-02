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
    private SingleLiveEvent<Integer> mMessageLiveData;
    private SingleLiveEvent<Void> mFragmentCloseLiveData;
    private MutableLiveData<List<Word>> mWordsLiveData;
    private CategoryRepository mRepository;
    private String mOldCategoryName;
    private int mWordIndex;


    public CategoryEditViewModel(String categoryName) {
        mRepository = CategoryRepositoryImpl.getInstance();
        mOldCategoryName = categoryName;
        this.categoryName.set(categoryName);
        mWordsLiveData = new MutableLiveData<>();
        mWordsLiveData.setValue(mRepository.getWordsByCategory(categoryName));
        mMessageLiveData = new SingleLiveEvent<>();
        mFragmentCloseLiveData = new SingleLiveEvent<>();
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onBtnSaveCategoryClick() {
        String newCategoryName = categoryName.get().trim();
        if (TextUtils.isEmpty(newCategoryName)) {
            showMessage(R.string.cef_toast_save_edit_category);
        } else {
            if (TextUtils.isEmpty(mOldCategoryName)) {
                addNewCategory(newCategoryName, mWordsLiveData.getValue());
            } else {
                updateCategory(mOldCategoryName, newCategoryName, mWordsLiveData.getValue());
            }
            mFragmentCloseLiveData.call();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onBtnSaveWordClick() {
        if (isTextFieldsNotEmpty()) {
            Word newWord = new Word(lexeme.get().trim(), translation.get().trim());
            List<Word> wordList = mWordsLiveData.getValue();
            if (mWordIndex >= 0) {
                wordList.set(mWordIndex, newWord);
            } else {
                wordList.add(newWord);
            }
            mWordsLiveData.setValue(wordList);
            cleanTextFields();
        } else {
            showMessage(R.string.cef_toast_save_word_empty_fields);
        }
    }

    public void onBtnCleanClick() {
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onIconRemoveWordClick(Word word) {
        List<Word> wordList = mWordsLiveData.getValue();
        wordList.remove(word);
        mWordsLiveData.setValue(wordList);
        cleanTextFields();
    }

    @SuppressWarnings("ConstantConditions")
    public void onRvItemClick(Word word) {
        lexeme.set(word.getLexeme());
        translation.set(word.getTranslation());
        mWordIndex = mWordsLiveData.getValue().indexOf(word);
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

    private void showMessage(@StringRes int resId) {
        mMessageLiveData.setValue(resId);
    }

    private void addNewCategory(String categoryName, List<Word> wordList) {
        mRepository.addNewCategory(categoryName, wordList);
    }

    private void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        mRepository.updateCategory(oldCategoryName, newCategoryName, wordList);
    }

    LiveData<List<Word>> getWordsLiveData() {
        return mWordsLiveData;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }

    LiveData<Void> getFragmentCloseLiveData() {
        return mFragmentCloseLiveData;
    }
}
