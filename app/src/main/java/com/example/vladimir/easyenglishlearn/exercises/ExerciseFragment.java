package com.example.vladimir.easyenglishlearn.exercises;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.model.Answer;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public abstract class ExerciseFragment extends Fragment {

    protected List<Word> mResultWordsList;
    protected int mIteration;
    private int mErrorsCount;
    protected boolean mTranslationDirection;
    protected StringBuilder mAnswerBuilder;
    protected Unbinder unbinder;


    @LayoutRes
    abstract int getLayoutResId();

    abstract void initializeAnswers();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, view);

        mAnswerBuilder = new StringBuilder();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mResultWordsList = bundle.getParcelableArrayList(SELECTED_WORDS);
            mTranslationDirection = bundle.getBoolean(TRANSLATION_DIRECTION, false);
            Collections.shuffle(mResultWordsList);
            initializeAnswers();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected boolean isExerciseOver() {
        Word currentWord = mResultWordsList.get(mIteration);
        Answer answer = new Answer(currentWord, mAnswerBuilder, mTranslationDirection);
        if (answer.isCorrect()) {
            if (++mIteration >= mResultWordsList.size()) {
                finishExercise();
                return true;
            }
        } else {
            showMessage(R.string.wrong_answer, -1);
            mErrorsCount++;
        }
        mAnswerBuilder.delete(0, mAnswerBuilder.length());
        return false;
    }

    protected void setQuestionToTextView(Word currentWord, TextView textView) {
        textView.setText(mTranslationDirection
                ? currentWord.getLexeme()
                : currentWord.getTranslation());
    }

    private void finishExercise() {
        showMessage(R.string.errors_count, mErrorsCount);
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private void showMessage(@StringRes int resId, int errorsCount) {
        String message = getString(resId);
        if (errorsCount >= 0) message += errorsCount;
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
