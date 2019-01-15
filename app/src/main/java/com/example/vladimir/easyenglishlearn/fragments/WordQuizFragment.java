package com.example.vladimir.easyenglishlearn.fragments;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordQuizFragment extends ExerciseFragment {

    @BindView(R.id.wqf_rg_translation)
    RadioGroup mRgTranslation;
    @BindView(R.id.wqf_tv_question)
    TextView mTvQuestion;


    public static Fragment newInstance(ArrayList<Word> selectedWordList,
                                       boolean translationDirection) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECTED_WORDS, selectedWordList);
        args.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        Fragment fragment = new WordQuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.wqf_rb_first, R.id.wqf_rb_second, R.id.wqf_rb_third})
    public void onRadioButtonAnswerClick(RadioButton radioButton) {
        mAnswerBuilder.append(radioButton.getText());
        if (isExerciseOver()) return;

        fillRadioGroup(mResultWordsList.get(mIteration));
    }

    @Override
    void initializeAnswers() {
        fillRadioGroup(mResultWordsList.get(mIteration));
    }

    @LayoutRes
    @Override
    int getLayoutResId() {
        return R.layout.fragment_word_quiz;
    }

    private void fillRadioGroup(Word currentWord) {
        setQuestionToTextView(currentWord, mTvQuestion);
        List<Word> tempWordsList = new ArrayList<>(mResultWordsList);
        mRgTranslation.clearCheck();
        Collections.shuffle(tempWordsList);
        int answersCount = mRgTranslation.getChildCount();
        int rightAnswerPosition = new Random().nextInt(answersCount);
        for (int i = 0; i < answersCount; i++) {
            RadioButton radioButton = (RadioButton) mRgTranslation.getChildAt(i);
            if (i == rightAnswerPosition) {
                setAnswerToRadioButton(currentWord, radioButton);
            } else {
                if (!tempWordsList.get(i).equals(currentWord)) {
                    setAnswerToRadioButton(tempWordsList.get(i), radioButton);
                } else {
                    setAnswerToRadioButton(tempWordsList.get(tempWordsList.size() - 1), radioButton);
                }
            }
        }
    }

    private void setAnswerToRadioButton(Word word, RadioButton radioButton) {
        radioButton.setText(mTranslationDirection
                ? word.getTranslation()
                : word.getLexeme());
    }
}
