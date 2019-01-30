package com.example.vladimir.easyenglishlearn.exercises;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.example.vladimir.easyenglishlearn.SingleLiveEvent;
import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordQuizViewModel extends ViewModel {

    public final ObservableField<String> question = new ObservableField<>();
    public final ObservableField<List<String>> answers = new ObservableField<>();
    private SingleLiveEvent<Void> mExerciseCloseLiveData;
    private SingleLiveEvent<Integer> mMessageLiveData;
    private SingleLiveEvent<Void> mClearRadioGroupLiveData;
    private List<Word> mWordList;
    private int mIteration;
    private int mErrorsCount;
    private boolean mTranslationDirection;
    private StringBuilder mAnswerBuilder;
    private static final int ANSWERS_COUNT = 3;


    public WordQuizViewModel(List<Word> wordList, boolean translationDirection) {
        mWordList = wordList;
        mTranslationDirection = translationDirection;
        mAnswerBuilder = new StringBuilder();
        mExerciseCloseLiveData = new SingleLiveEvent<>();
        mMessageLiveData = new SingleLiveEvent<>();
        mClearRadioGroupLiveData = new SingleLiveEvent<>();

        prepareAnswers(mWordList.get(mIteration));
    }

    public void onAnswerChecked(String answer) {
        mAnswerBuilder.append(answer);
        if (isExerciseOver()) {
            finishExercise();
        } else {
            prepareAnswers(mWordList.get(mIteration));
        }
    }

    private boolean isExerciseOver() {
        Word currentWord = mWordList.get(mIteration);
        Answer answer = new Answer(currentWord, mAnswerBuilder, mTranslationDirection);
        if (answer.isCorrect()) {
            if (++mIteration >= mWordList.size()) {
                return true;
            }
        } else {
            showMessage(-1);
            mErrorsCount++;
        }
        mAnswerBuilder.delete(0, mAnswerBuilder.length());
        return false;
    }

    private void finishExercise() {
        showMessage(mErrorsCount);
        mExerciseCloseLiveData.call();
        mErrorsCount = 0;
        mIteration = 0;
    }

    private void showMessage(int errorsCount) {
        mMessageLiveData.setValue(errorsCount);
    }

    private void prepareAnswers(Word currentWord) {
        mClearRadioGroupLiveData.call();
        question.set(mTranslationDirection ? currentWord.getLexeme() : currentWord.getTranslation());
        List<String> answerList = new ArrayList<>();
        addAnswer(answerList, currentWord);
        while (answerList.size() < ANSWERS_COUNT) {
            int randomIndex = new Random().nextInt(mWordList.size());
            Word tempWord = mWordList.get(randomIndex);
            if (isWordNotInAnswerList(answerList, tempWord)) addAnswer(answerList, tempWord);
        }
        Collections.shuffle(answerList);
        answers.set(answerList);
    }

    private boolean isWordNotInAnswerList(List<String> answerList, Word word) {
        return !answerList.contains(mTranslationDirection
                ? word.getTranslation()
                : word.getLexeme());
    }

    private void addAnswer(List<String> answerList, Word word) {
        answerList.add(mTranslationDirection
                ? word.getTranslation()
                : word.getLexeme());
    }

    LiveData<Void> getExerciseCloseLiveData() {
        return mExerciseCloseLiveData;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }

    LiveData<Void> getClearRadioGroupLiveData() {
        return mClearRadioGroupLiveData;
    }
}
