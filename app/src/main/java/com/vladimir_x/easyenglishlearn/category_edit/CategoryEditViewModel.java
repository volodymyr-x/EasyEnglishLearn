package com.vladimir_x.easyenglishlearn.category_edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableField;
import androidx.annotation.StringRes;
import android.text.TextUtils;

import com.vladimir_x.easyenglishlearn.R;
import com.vladimir_x.easyenglishlearn.App;
import com.vladimir_x.easyenglishlearn.SingleLiveEvent;
import com.vladimir_x.easyenglishlearn.db.WordDao;
import com.vladimir_x.easyenglishlearn.model.Word;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.vladimir_x.easyenglishlearn.Constants.EMPTY_STRING;

public class CategoryEditViewModel extends ViewModel {

    public final ObservableField<String> categoryName = new ObservableField<>();
    public final ObservableField<String> lexeme = new ObservableField<>();
    public final ObservableField<String> translation = new ObservableField<>();
    private SingleLiveEvent<Integer> mMessageLiveData;
    private SingleLiveEvent<Void> mFragmentCloseLiveData;
    private MutableLiveData<List<Word>> mWordsLiveData;
    private WordDao mRepository;
    private CompositeDisposable mDisposable;
    private String mOldCategoryName;
    private int mWordIndex;


    public CategoryEditViewModel(String categoryName) {
        mRepository = App.getInstance().getDatabase().wordDao();
        mOldCategoryName = categoryName;
        this.categoryName.set(categoryName);
        mWordsLiveData = new MutableLiveData<>();
        mMessageLiveData = new SingleLiveEvent<>();
        mFragmentCloseLiveData = new SingleLiveEvent<>();
        mDisposable = new CompositeDisposable();

        cleanTextFields();
        subscribeWordsToData();
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

    private void subscribeWordsToData() {
        Disposable disposable = mRepository.getWordsByCategory(mOldCategoryName)
                .subscribeOn(Schedulers.io())
                .subscribe(words -> mWordsLiveData.postValue(words));
        mDisposable.add(disposable);
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
        Disposable disposable = Completable
                .fromAction(() -> mRepository.addNewCategory(wordList, categoryName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> showMessage(R.string.category_added));
        mDisposable.add(disposable);
    }

    private void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        Disposable disposable = Completable
                .fromAction(() -> mRepository.updateCategory(oldCategoryName, newCategoryName, wordList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> showMessage(R.string.category_edited));
        mDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        mDisposable.dispose();

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
