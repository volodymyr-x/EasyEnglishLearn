package com.example.vladimir.easyenglishlearn.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.R;

public class RemoveCategoryFragment extends DialogFragment {

    private Button btnYes, btnNo;
    private TextView tvRemoveCategory;
    private RemoveCategoryListener listener;

    public static final String DIALOG_REMOVE_CATEGORY = "DIALOG_REMOVE_CATEGORY";

    public interface RemoveCategoryListener {
        void okBtnClicked();
    }

    private OnClickListener btnNoListener = v -> dismiss();

    private OnClickListener btnYesListener = v -> {
        listener.okBtnClicked();
        dismiss();
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (RemoveCategoryListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.remove_category, container);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        float fontSize  = Float.parseFloat(prefs.getString(getString(R.string.pr_size_list), "20"));
        btnYes.setTextSize(fontSize);
        btnNo.setTextSize(fontSize);
        tvRemoveCategory.setTextSize(fontSize);
    }

}
