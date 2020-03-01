package com.example.vladimir.easyenglishlearn.exercises;


import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentWordQuizBinding;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordQuizFragment extends Fragment {

    private FragmentWordQuizBinding mBinding;


    @NonNull
    public static Fragment newInstance(ArrayList<Word> selectedWordList,
                                       boolean translationDirection) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECTED_WORDS, selectedWordList);
        args.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        Fragment fragment = new WordQuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_word_quiz,
                container,
                false);
        WordQuizViewModel viewModel = new ViewModelProvider(this).get(WordQuizViewModel.class);
        mBinding.setViewModel(viewModel);
        subscribeToLiveData(viewModel);

        if (savedInstanceState == null) {
            boolean translationDirection = getArguments().getBoolean(TRANSLATION_DIRECTION);
            List<Word> wordList = getArguments().getParcelableArrayList(SELECTED_WORDS);
            viewModel.startExercise(wordList, translationDirection);
        }
        return mBinding.getRoot();
    }

    private void subscribeToLiveData(@NonNull WordQuizViewModel viewModel) {
        viewModel.getExerciseCloseLiveData().observe(getViewLifecycleOwner(), aVoid -> closeFragment());
        viewModel.getMessageLiveData().observe(getViewLifecycleOwner(), this::showMessage);
        viewModel.getClearRadioGroupLiveData().observe(getViewLifecycleOwner(), aVoid -> clearRadioGroup());
    }

    private void clearRadioGroup() {
        mBinding.wqfRgAnswers.clearCheck();
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
}
