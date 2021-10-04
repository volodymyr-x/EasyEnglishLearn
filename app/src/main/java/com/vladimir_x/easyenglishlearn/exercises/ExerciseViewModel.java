package com.vladimir_x.easyenglishlearn.exercises;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.databinding.ObservableField;

import com.vladimir_x.easyenglishlearn.SingleLiveEvent;
import com.vladimir_x.easyenglishlearn.model.Answer;
import com.vladimir_x.easyenglishlearn.model.Word;

import java.util.List;

public abstract class ExerciseViewModel extends ViewModel {

    private SingleLiveEvent<Void> mExerciseCloseLiveData;
    private SingleLiveEvent<Integer> mMessageLiveData;
    private int mIteration;
    private int mErrorsCount;
    boolean mTranslationDirection;
    List<Word> mWordList;
    StringBuilder mAnswerBuilder;
    Word mCurrentWord;
    public final ObservableField<String> question = new ObservableField<>();


    ExerciseViewModel() {
        mAnswerBuilder = new StringBuilder();
        mExerciseCloseLiveData = new SingleLiveEvent<>();
        mMessageLiveData = new SingleLiveEvent<>();
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

    void prepareQuestionAndAnswers() {
        mCurrentWord = mWordList.get(mIteration);
        question.set(mTranslationDirection
                ? mCurrentWord.getLexeme()
                : mCurrentWord.getTranslation());
        cleanPreviousData();
    }

    void checkAnswer() {
        if (isExerciseOver()) {
            finishExercise();
        } else {
            prepareQuestionAndAnswers();
        }
    }

    void cleanPreviousData() {
        mAnswerBuilder.delete(0, mAnswerBuilder.length());
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

    LiveData<Void> getExerciseCloseLiveData() {
        return mExerciseCloseLiveData;
    }

    LiveData<Integer> getMessageLiveData() {
        return mMessageLiveData;
    }
}
