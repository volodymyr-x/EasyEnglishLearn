package com.example.vladimir.easyenglishlearn.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.CategoryActivity;
import com.example.vladimir.easyenglishlearn.R;

public class RemoveCategoryFragment extends DialogFragment {

    private Button btnYes, btnNo;
    private TextView tvRemoveCategory;
    private CategoryActivity activity;

    public static final String DIALOG_REMOVE_CATEGORY = "DIALOG_REMOVE_CATEGORY";

    private OnClickListener btnNoListener = v -> dismiss();

    private OnClickListener btnYesListener = v -> {
        activity.removeSelectedCategory(activity.getSelectedColumnIndex());
        dismiss();
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remove_category, container);
        activity = (CategoryActivity) getActivity();
        btnYes = view.findViewById(R.id.rcf_btn_yes);
        btnYes.setOnClickListener(btnYesListener);
        btnNo = view.findViewById(R.id.rcf_btn_no);
        btnNo.setOnClickListener(btnNoListener);
        tvRemoveCategory = view.findViewById(R.id.rcf_tv_remove_category);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        float fontSize = activity.getFontSize();
        btnYes.setTextSize(fontSize);
        btnNo.setTextSize(fontSize);
        tvRemoveCategory.setTextSize(fontSize);
    }

}
