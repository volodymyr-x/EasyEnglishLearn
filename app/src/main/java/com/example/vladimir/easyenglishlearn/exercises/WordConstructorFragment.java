package com.example.vladimir.easyenglishlearn.exercises;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordConstructorFragment extends ExerciseFragment {

    @BindView(R.id.wcf_gridContainer)
    GridLayout mGridContainer;
    @BindView(R.id.wcf_tv_question)
    TextView mTvQuestion;
    @BindView(R.id.wcf_tv_answer)
    TextView mTvAnswer;


    private OnClickListener newButtonListener = v -> {
        mAnswerBuilder.append(((Button) v).getText().toString());
        mTvAnswer.setText(mAnswerBuilder);
        mGridContainer.removeView(v);
    };


    @NonNull
    public static Fragment newInstance(ArrayList<Word> selectedWordList,
                                       boolean translationDirection) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECTED_WORDS, selectedWordList);
        args.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        Fragment fragment = new WordConstructorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void initializeAnswers() {
        prepareButtons(mResultWordsList.get(mIteration));
    }

    @LayoutRes
    @Override
    int getLayoutResId() {
        return R.layout.fragment_word_constructor;
    }

    @OnClick(R.id.wcf_btn_clean)
    public void onButtonCleanClick() {
        if (mAnswerBuilder.length() > 0) {
            int lastCharIndex = mAnswerBuilder.length() - 1;
            char letterFromButton = mAnswerBuilder.charAt(lastCharIndex);
            mAnswerBuilder.deleteCharAt(lastCharIndex);
            mTvAnswer.setText(mAnswerBuilder);
            createButton(letterFromButton);
        }
    }

    @OnClick(R.id.wcf_btn_answer)
    public void onButtonAnswerClick() {
        if (isExerciseOver()) return;

        mTvAnswer.setText("");
        mGridContainer.removeAllViews();
        prepareButtons(mResultWordsList.get(mIteration));
    }


    private void prepareButtons(Word currentWord) {
        setQuestionToTextView(currentWord, mTvQuestion);
        char[] letters = splitWord(currentWord);
        shuffleArray(letters);
        for (char letter : letters) {
            createButton(letter);
        }
    }

    private char[] splitWord(Word currentWord) {
        return mTranslationDirection
                ? currentWord.getTranslation().toCharArray()
                : currentWord.getLexeme().toCharArray();
    }

    private void shuffleArray(char[] letters) {
        Random random = new Random();
        for (int i = letters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char c = letters[index];
            letters[index] = letters[i];
            letters[i] = c;
        }
    }

    private void createButton(char letter) {
        Button button = new Button(getActivity());
        button.setText(String.valueOf(letter));
        button.setOnClickListener(newButtonListener);
        /*button.setTextSize(Objects.requireNonNull(getActivity())
                .getBaseContext()
                .getResources()
                .getDimension(R.dimen.text_size));*/
        mGridContainer.addView(button, new LayoutParams(150, 150));
    }
}

