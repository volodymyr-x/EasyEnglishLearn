package com.example.vladimir.easyenglishlearn.exercises;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentWordConstructorBinding;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordConstructorFragment extends /*Exercise*/Fragment {

    private FragmentWordConstructorBinding mBinding;
    private WordConstructorViewModel mViewModel;


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

    private OnClickListener newButtonListener = v -> {
        String letter = ((Button) v).getText().toString();
        mViewModel.onNewButtonClick(letter);
        mBinding.wcfGridContainer.removeView(v);
    };

    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_word_constructor,
                container,
                false);
        boolean translationDirection = getArguments().getBoolean(TRANSLATION_DIRECTION);
        List<Word> wordList = getArguments().getParcelableArrayList(SELECTED_WORDS);

        mViewModel = ViewModelProviders.of(this).get(WordConstructorViewModel.class);
        mBinding.setViewModel(mViewModel);

        subscribeToLiveData(mViewModel);
        if (savedInstanceState == null) {
            mViewModel.startExercise(wordList, translationDirection);
        }
        return mBinding.getRoot();
    }

    private void subscribeToLiveData(@NonNull WordConstructorViewModel viewModel) {
        viewModel.getExerciseCloseLiveData().observe(this, aVoid -> closeFragment());
        viewModel.getMessageLiveData().observe(this, this::showMessage);
        viewModel.getCharArrayLiveData().observe(this, this::createButtons);
    }

    private void closeFragment() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private void showMessage(int errorsCount) {
        String message;
        if (errorsCount < 0) {
            message = getString(R.string.wrong_answer);
        } else {
            message = getString(R.string.errors_count, errorsCount);
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void createButtons(List<Character> letters) {
        mBinding.wcfGridContainer.removeAllViews();
        for (char letter : letters) {
            Button button = new Button(getActivity());
            button.setText(String.valueOf(letter));
            button.setOnClickListener(newButtonListener);
        /*button.setTextSize(Objects.requireNonNull(getActivity())
                .getBaseContext()
                .getResources()
                .getDimension(R.dimen.text_size));*/
            mBinding.wcfGridContainer.addView(button, new LayoutParams(150, 150));
        }
    }
}

