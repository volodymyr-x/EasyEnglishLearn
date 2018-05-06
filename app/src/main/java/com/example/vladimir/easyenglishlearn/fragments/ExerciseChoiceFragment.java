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

import com.example.vladimir.easyenglishlearn.MainActivity;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.WordConstructorActivity;
import com.example.vladimir.easyenglishlearn.WordQuizActivity;
import com.example.vladimir.easyenglishlearn.WordSelectionActivity;

public class ExerciseChoiceFragment extends DialogFragment implements OnClickListener, OnCheckedChangeListener{

    private RadioButton rbRuEn, rbEnRu;
    private Button btnConstructor, btnQuiz, btnCancel;
    private TextView tvTitle;
    private boolean translationDirection;
    public static final String SELECTED_WORDS = "SELECTED_WORDS";
    public static final String TRANSLATION_DIRECTION = "TRANSLATION_DIRECTION";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_choice, container);
        tvTitle = view.findViewById(R.id.tvTitle);
        ((RadioGroup) view.findViewById(R.id.rg_translation_direction)).setOnCheckedChangeListener(this);
        rbEnRu = view.findViewById(R.id.rbEnRu);
        rbRuEn = view.findViewById(R.id.rbRuEn);
        rbRuEn.setChecked(true);
        btnQuiz = view.findViewById(R.id.ecf_btn_quiz);
        btnQuiz.setOnClickListener(this);
        btnConstructor = view.findViewById(R.id.ecf_btn_constructor);
        btnConstructor.setOnClickListener(this);
        btnCancel = view.findViewById(R.id.ecf_btn_cancel);
        btnCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        float fontSize = MainActivity.fontSize;
        rbRuEn.setTextSize(fontSize);
        rbEnRu.setTextSize(fontSize);
        btnQuiz.setTextSize(fontSize);
        btnConstructor.setTextSize(fontSize);
        btnCancel.setTextSize(fontSize);
        tvTitle.setTextSize(fontSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ecf_btn_constructor:
                startActivity(intentLoad(WordConstructorActivity.class));
                break;
            case R.id.ecf_btn_quiz:
                startActivity(intentLoad(WordQuizActivity.class));
                break;
            case R.id.ecf_btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private Intent intentLoad (Class clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SELECTED_WORDS,
                ((WordSelectionActivity) getActivity()).getSelectedWordsList());
        bundle.putBoolean(TRANSLATION_DIRECTION, translationDirection);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        translationDirection = true;
        switch (checkedId) {
            case R.id.rbEnRu:
                translationDirection = true;
                break;
            case R.id.rbRuEn:
                translationDirection = false;
                break;
            default: break;
        }
    }
}

