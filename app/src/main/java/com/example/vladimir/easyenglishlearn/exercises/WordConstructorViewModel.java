package com.example.vladimir.easyenglishlearn.exercises;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class WordConstructorViewModel extends ViewModel {

    public final ObservableField<String> question = new ObservableField<>();
    public final ObservableField<String> answer = new ObservableField<>();
    private SingleLiveEvent<Void> mExerciseCloseLiveData;
    private SingleLiveEvent<Integer> mMessageLiveData;
    private MutableLiveData<List<Character>> mCharArrayLiveData;
    private List<Word> mWordList;
    private int mIteration;
    private int mErrorsCount;
    private boolean mTranslationDirection;
    private StringBuilder mAnswerBuilder;
    private List<Character> mLetterList;
    private Word mCurrentWord;


    public WordConstructorViewModel() {
        mAnswerBuilder = new StringBuilder();
        mLetterList = new ArrayList<>();
        mExerciseCloseLiveData = new SingleLiveEvent<>();
        mMessageLiveData = new SingleLiveEvent<>();
        mCharArrayLiveData = new MutableLiveData<>();
    }

    void onNewButtonClick(String letter) {
        mAnswerBuilder.append(letter);
        answer.set(mAnswerBuilder.toString());
        mLetterList.remove(Character.valueOf(letter.charAt(0)));
        if (mLetterList.isEmpty()) {
            checkAnswer();
        }
    }

    public void onButtonUndoClick() {
        if (mAnswerBuilder.length() > 0) {
            int lastCharIndex = mAnswerBuilder.length() - 1;
            char letterFromButton = mAnswerBuilder.charAt(lastCharIndex);
            mAnswerBuilder.deleteCharAt(lastCharIndex);
            answer.set(mAnswerBuilder.toString());
            mLetterList.add(letterFromButton);
            mCharArrayLiveData.setValue(mLetterList);
        }
    }

    /**
     * This method must be called from fragment  when it is first started
     */
    void startExercise(List<Word> wordList, boolean translationDirection) {
        mWordList = wordList;
        mTranslationDirection = translationDirection;
        mErrorsCount = 0;
        mIteration = 0;
        prepareQuestionAndAnswers();
    }

    private void checkAnswer() {
        if (isExerciseOver()) {
            finishExercise();
        } else {
            prepareQuestionAndAnswers();
        }
    }

    private boolean isExerciseOver() {
        Answer answer = new Answer(mCurrentWord, mAnswerBuilder, mTranslationDirection);
        if (answer.isCorrect()) {
            return ++mIteration >= mWordList.size();
        } else {
            showMessage(-1);
            mErrorsCount++;
        }
        return false;
    }

    private void finishExercise() {
        showMessage(mErrorsCount);
        mExerciseCloseLiveData.call();
    }

    private void showMessage(int errorsCount) {
        mMessageLiveData.setValue(errorsCount);
    }

    private void prepareQuestionAndAnswers() {
        mCurrentWord = mWordList.get(mIteration);
        cleanPreviousData();
        question.set(mTranslationDirection
                ? mCurrentWord.getLexeme()
                : mCurrentWord.getTranslation());
        splitCurrentWord();
        Collections.shuffle(mLetterList);
        mCharArrayLiveData.setValue(mLetterList);
    }

    private void cleanPreviousData() {
        mLetterList.clear();
        answer.set(EMPTY_STRING);
        mAnswerBuilder.delete(0, mAnswerBuilder.length());
    }

    private void splitCurrentWord() {
        char[] letters = mTranslationDirection
                ? mCurrentWord.getTranslation().toCharArray()
                : mCurrentWord.getLexeme().toCharArray();
        for (char letter : letters) {
            mLetterList.add(letter);
        }
    }

    LiveData<Void> getExerciseCloseLiveData() {
        return mExerciseCloseLiveData;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }

    LiveData<List<Character>> getCharArrayLiveData() {
        return mCharArrayLiveData;
    }
}
