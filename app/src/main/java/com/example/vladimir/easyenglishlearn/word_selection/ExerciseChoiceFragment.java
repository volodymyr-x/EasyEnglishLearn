package com.example.vladimir.easyenglishlearn.word_selection;


import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vladimir.easyenglishlearn.ModelFactory;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentExerciseChoiceBinding;

import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.ARG_CATEGORY_NAME;

public class ExerciseChoiceFragment extends DialogFragment {


    @NonNull
    public static DialogFragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        DialogFragment fragment = new ExerciseChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentExerciseChoiceBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_exercise_choice,
                container,
                false);
        String categoryName = Objects.requireNonNull(getArguments()).getString(ARG_CATEGORY_NAME);
        WordSelectionViewModel viewModel = ViewModelProviders
                .of(this, ModelFactory.getInstance(categoryName))
                .get(WordSelectionViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getCloseDialogLiveData().observe(this, aVoid -> closeDialog());
        return binding.getRoot();
    }

    private void closeDialog() {
        dismiss();
    }
}

