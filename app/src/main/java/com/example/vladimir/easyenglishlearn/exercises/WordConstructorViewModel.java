package com.example.vladimir.easyenglishlearn.exercises;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.vladimir.easyenglishlearn.Constants.EMPTY_STRING;

public class WordConstructorViewModel extends ExerciseViewModel {

    private MutableLiveData<List<Character>> mCharArrayLiveData;
    private List<Character> mLetterList;
    public final ObservableField<String> answer = new ObservableField<>();


    public WordConstructorViewModel() {
        mLetterList = new ArrayList<>();
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

    @Override
    void prepareQuestionAndAnswers() {
        super.prepareQuestionAndAnswers();

        splitCurrentWord();
        Collections.shuffle(mLetterList);
        mCharArrayLiveData.setValue(mLetterList);
    }

    @Override
    void cleanPreviousData() {
        super.cleanPreviousData();
        mLetterList.clear();
        answer.set(EMPTY_STRING);
    }

    private void splitCurrentWord() {
        char[] letters = mTranslationDirection
                ? mCurrentWord.getTranslation().toCharArray()
                : mCurrentWord.getLexeme().toCharArray();
        for (char letter : letters) {
            mLetterList.add(letter);
        }
    }

    LiveData<List<Character>> getCharArrayLiveData() {
        return mCharArrayLiveData;
    }
}
