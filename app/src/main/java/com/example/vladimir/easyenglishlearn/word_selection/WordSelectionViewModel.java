package com.example.vladimir.easyenglishlearn.word_selection;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.vladimir.easyenglishlearn.App;
import com.example.vladimir.easyenglishlearn.Constants.Exercises;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.db.WordDao;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.vladimir.easyenglishlearn.Constants.ANSWERS_COUNT;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_CONSTRUCTOR;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_QUIZ;

public class WordSelectionViewModel extends ViewModel {

    private SingleLiveEvent<Integer> mMessageLiveData;
    private SingleLiveEvent<String> mChoiceDialogLiveData;
    private MutableLiveData<List<Word>> mWordsLiveData;
    private SingleLiveEvent<WordSelectionDto> mSelectedWordsLiveData;
    private SingleLiveEvent<Void> mCloseDialogLiveData;
    private ArrayList<Word> mSelectedWordList;
    private CompositeDisposable mDisposable;
    private String mCategoryName;
    private boolean mTranslationDirection = true;


    public WordSelectionViewModel(String categoryName) {
        mCategoryName = categoryName;
        WordDao repository = App.getInstance().getDatabase().wordDao();
        mSelectedWordsLiveData = new SingleLiveEvent<>();
        mMessageLiveData = new SingleLiveEvent<>();
        mChoiceDialogLiveData = new SingleLiveEvent<>();
        mWordsLiveData = new MutableLiveData<>();
        mCloseDialogLiveData = new SingleLiveEvent<>();
        mSelectedWordList = new ArrayList<>();
        mDisposable = new CompositeDisposable();

        subscribeWordsToData(repository);
    }

    public void onBtnStartClick() {
        if (mSelectedWordList.size() < ANSWERS_COUNT) {
            showMessage();
        } else {
            mChoiceDialogLiveData.setValue(mCategoryName);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onChooseAllChange(boolean checked) {
        mSelectedWordList.clear();
        List<Word> wordList = mWordsLiveData.getValue();
        if (checked) {
            mSelectedWordList.addAll(wordList);
        }
        for (Word word : wordList) {
            word.setChecked(checked);
        }
        mWordsLiveData.setValue(wordList);
    }

    public void onItemCheckBoxChange(boolean checked, Word word) {
        if (checked) {
            if (!mSelectedWordList.contains(word)) mSelectedWordList.add(word);
        } else {
            mSelectedWordList.remove(word);
        }
        word.setChecked(checked);
    }

    public void onBtnConstructorClick() {
        sendDTO(WORD_CONSTRUCTOR);
    }

    public void onBtnQuizClick() {
        sendDTO(WORD_QUIZ);
    }

    public void onBtnCancelClick() {
        mCloseDialogLiveData.call();
    }

    public void onDirectionChanged(int checkedId) {
        switch (checkedId) {
            case R.id.ecf_rb_en_ru:
                mTranslationDirection = true;
                break;
            case R.id.ecf_rb_ru_en:
                mTranslationDirection = false;
                break;
            default:
                break;
        }
    }

    private void subscribeWordsToData(WordDao repository) {
        Disposable disposable = repository.getWordsByCategory(mCategoryName)
                .subscribeOn(Schedulers.io())
                .subscribe(words -> mWordsLiveData.postValue(words));
        mDisposable.add(disposable);
    }

    private void sendDTO(@Exercises String exerciseType) {
        mCloseDialogLiveData.call();
        WordSelectionDto dto = new WordSelectionDto(mTranslationDirection,
                mSelectedWordList, exerciseType);
        mSelectedWordsLiveData.setValue(dto);
    }

    private void showMessage() {
        mMessageLiveData.setValue(R.string.wsa_toast_min_words_count);
    }

    @Override
    protected void onCleared() {
        mDisposable.dispose();

    }

    public String getCategoryName() {
        return mCategoryName;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }

    LiveData<String> getChoiceDialogLiveData() {
        return mChoiceDialogLiveData;
    }

    LiveData<List<Word>> getWordsLiveData() {
        return mWordsLiveData;
    }

    LiveData<Void> getCloseDialogLiveData() {
        return mCloseDialogLiveData;
    }

    LiveData<WordSelectionDto> getSelectedWordsLiveData() {
        return mSelectedWordsLiveData;
    }
}
