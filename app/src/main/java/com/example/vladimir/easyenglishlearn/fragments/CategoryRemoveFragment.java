package com.example.vladimir.easyenglishlearn.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.vladimir.easyenglishlearn.Constants.CATEGORY_NAME;

public class CategoryRemoveFragment extends DialogFragment {

    @BindView(R.id.rcf_tv_remove_category)
    TextView tvRemoveCategory;
    private String mCategoryName;


    public static DialogFragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, categoryName);
        DialogFragment fragment = new CategoryRemoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remove_category, container);
        ButterKnife.bind(this, view);
        mCategoryName = Objects.requireNonNull(getArguments()).getString(CATEGORY_NAME);
        tvRemoveCategory.setText(mCategoryName);
        return view;
    }

    @OnClick(R.id.rcf_btn_yes)
    public void onButtonOKClick() {
        if (getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_NAME, mCategoryName);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }

    @OnClick(R.id.rcf_btn_no)
    public void onButtonCancelClick() {
        dismiss();
    }
}
