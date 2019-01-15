package com.example.vladimir.easyenglishlearn.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.vladimir.easyenglishlearn.Constants.Exercises;
import com.example.vladimir.easyenglishlearn.R;

import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_TYPE;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_CONSTRUCTOR;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_QUIZ;

public class ExerciseChoiceFragment extends DialogFragment {

    private boolean mTranslationDirection = true;


    public static DialogFragment newInstance() {
        return new ExerciseChoiceFragment();
    }

    private OnClickListener btnConstructorListener = v -> {
        sendResult(WORD_CONSTRUCTOR);
        dismiss();
    };

    private OnClickListener btnQuizListener = v -> {
        sendResult(WORD_QUIZ);
        dismiss();
    };

    private OnClickListener btnCancelListener = v -> dismiss();

    private OnCheckedChangeListener rgChangeListener = (group, checkedId) -> {
        switch (checkedId) {
            case R.id.ecf_rb_en_ru:
                mTranslationDirection = true;
                break;
            case R.id.ecf_rb_ru_en:
                mTranslationDirection = false;
                break;
            default:
                break;
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_choice, container);
        ((RadioGroup) view.findViewById(R.id.ecf_rg_translation_direction))
                .setOnCheckedChangeListener(rgChangeListener);

        Button btnQuiz = view.findViewById(R.id.ecf_btn_quiz);
        btnQuiz.setOnClickListener(btnQuizListener);
        Button btnConstructor = view.findViewById(R.id.ecf_btn_constructor);
        btnConstructor.setOnClickListener(btnConstructorListener);
        Button btnCancel = view.findViewById(R.id.ecf_btn_cancel);
        btnCancel.setOnClickListener(btnCancelListener);
        return view;
    }

    private void sendResult(@Exercises String exerciseType) {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(TRANSLATION_DIRECTION, mTranslationDirection);
        intent.putExtra(EXERCISE_TYPE, exerciseType);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}

