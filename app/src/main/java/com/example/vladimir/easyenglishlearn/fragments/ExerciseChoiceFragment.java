package com.example.vladimir.easyenglishlearn.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
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

public class ExerciseChoiceFragment extends DialogFragment {

    private RadioButton rbRuEn, rbEnRu;
    private Button btnConstructor, btnQuiz, btnCancel;
    private TextView tvTitle, tvTranslationDirection;
    private boolean translationDirection = true;
    private ExerciseChoiceListener listener;

    public interface ExerciseChoiceListener {
        void btnConstructorClicked(boolean translationDirection);
        void btnQuizClicked(boolean translationDirection);
    }

    private OnClickListener btnConstructorListener = v -> {
        listener.btnConstructorClicked(translationDirection);
        dismiss();
    };

    private OnClickListener btnQuizListener = v -> {
        listener.btnQuizClicked(translationDirection);
        dismiss();
    };

    private OnClickListener btnCancelListener = v -> dismiss();

    private OnCheckedChangeListener rgChangeListener = (group, checkedId) -> {
        switch (checkedId) {
            case R.id.ecf_rb_en_ru:
                translationDirection = true;
                break;
            case R.id.ecf_rb_ru_en:
                translationDirection = false;
                break;
            default: break;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ExerciseChoiceListener) context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_choice, container);
        tvTitle = view.findViewById(R.id.ecf_tv_title);
        tvTranslationDirection = view.findViewById(R.id.ecf_tv_translation_direction);
        ((RadioGroup) view.findViewById(R.id.ecf_rg_translation_direction))
                .setOnCheckedChangeListener(rgChangeListener);
        rbEnRu = view.findViewById(R.id.ecf_rb_en_ru);
        rbRuEn = view.findViewById(R.id.ecf_rb_ru_en);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        float fontSize  = Float.parseFloat(prefs.getString(getString(R.string.pr_size_list),
                getString(R.string.pr_default_size)));
        rbRuEn.setTextSize(fontSize);
        rbEnRu.setTextSize(fontSize);
        btnQuiz.setTextSize(fontSize);
        btnConstructor.setTextSize(fontSize);
        btnCancel.setTextSize(fontSize);
        tvTitle.setTextSize(fontSize);
        tvTranslationDirection.setTextSize(fontSize);
    }
}

