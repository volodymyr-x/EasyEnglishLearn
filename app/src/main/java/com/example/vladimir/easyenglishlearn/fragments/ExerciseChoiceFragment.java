package com.example.vladimir.easyenglishlearn.fragments;


import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.WordConstructorActivity;
import com.example.vladimir.easyenglishlearn.WordQuizActivity;
import com.example.vladimir.easyenglishlearn.WordSelectionActivity;

public class ExerciseChoiceFragment extends DialogFragment {

    private RadioButton rbRuEn, rbEnRu;
    private Button btnConstructor, btnQuiz, btnCancel;
    private TextView tvTitle;
    private boolean translationDirection = true;
    private WordSelectionActivity activity;

    public static final String SELECTED_WORDS = "SELECTED_WORDS";
    public static final String TRANSLATION_DIRECTION = "TRANSLATION_DIRECTION";

    private OnClickListener btnConstructorListener = v -> {
        startActivity(intentLoad(WordConstructorActivity.class));
        dismiss();
    };

    private OnClickListener btnQuizListener = v -> {
        startActivity(intentLoad(WordQuizActivity.class));
        dismiss();
    };

    private OnClickListener btnCancelListener = v -> dismiss();

    private OnCheckedChangeListener rgChangeListener = (group, checkedId) -> {
        switch (checkedId) {
            case R.id.rbEnRu:
                translationDirection = true;
                break;
            case R.id.rbRuEn:
                translationDirection = false;
                break;
            default: break;
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_choice, container);
        activity = (WordSelectionActivity) getActivity();
        tvTitle = view.findViewById(R.id.tvTitle);
        ((RadioGroup) view.findViewById(R.id.rg_translation_direction))
                .setOnCheckedChangeListener(rgChangeListener);
        rbEnRu = view.findViewById(R.id.rbEnRu);
        rbRuEn = view.findViewById(R.id.rbRuEn);
        rbRuEn.setChecked(true);
        btnQuiz = view.findViewById(R.id.ecf_btn_quiz);
        btnQuiz.setOnClickListener(btnQuizListener);
        btnConstructor = view.findViewById(R.id.ecf_btn_constructor);
        btnConstructor.setOnClickListener(btnConstructorListener);
        btnCancel = view.findViewById(R.id.ecf_btn_cancel);
        btnCancel.setOnClickListener(btnCancelListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        float fontSize = activity.getFontSize();
        rbRuEn.setTextSize(fontSize);
        rbEnRu.setTextSize(fontSize);
        btnQuiz.setTextSize(fontSize);
        btnConstructor.setTextSize(fontSize);
        btnCancel.setTextSize(fontSize);
        tvTitle.setTextSize(fontSize);
    }

    private Intent intentLoad (Class clazz) {
        Intent intent = new Intent(activity, clazz);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SELECTED_WORDS, activity.getSelectedWordsList());
        bundle.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        intent.putExtras(bundle);
        return intent;
    }
}

