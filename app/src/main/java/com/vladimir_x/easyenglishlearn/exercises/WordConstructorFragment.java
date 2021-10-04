package com.vladimir_x.easyenglishlearn.exercises;

import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.vladimir_x.easyenglishlearn.R;
import com.vladimir_x.easyenglishlearn.databinding.FragmentWordConstructorBinding;
import com.vladimir_x.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vladimir_x.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.vladimir_x.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordConstructorFragment extends Fragment {

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
        mViewModel = new ViewModelProvider(this).get(WordConstructorViewModel.class);
        mBinding.setViewModel(mViewModel);
        subscribeToLiveData(mViewModel);

        if (savedInstanceState == null) {
            boolean translationDirection = getArguments().getBoolean(TRANSLATION_DIRECTION);
            List<Word> wordList = getArguments().getParcelableArrayList(SELECTED_WORDS);
            mViewModel.startExercise(wordList, translationDirection);
        }
        return mBinding.getRoot();
    }

    private void subscribeToLiveData(@NonNull WordConstructorViewModel viewModel) {
        viewModel.getExerciseCloseLiveData().observe(getViewLifecycleOwner(), aVoid -> closeFragment());
        viewModel.getMessageLiveData().observe(getViewLifecycleOwner(), this::showMessage);
        viewModel.getCharArrayLiveData().observe(getViewLifecycleOwner(), this::createButtons);
    }

    private void closeFragment() {
        requireActivity().onBackPressed();
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

