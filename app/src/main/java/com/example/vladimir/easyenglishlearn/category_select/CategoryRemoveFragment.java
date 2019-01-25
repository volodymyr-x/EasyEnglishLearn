package com.example.vladimir.easyenglishlearn.category_select;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentRemoveCategoryBinding;

import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.CATEGORY_NAME;

public class CategoryRemoveFragment extends DialogFragment {

    private CategoryViewModel mViewModel;


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
        FragmentRemoveCategoryBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_remove_category,
                container,
                false);
        String categoryName = Objects.requireNonNull(getArguments()).getString(CATEGORY_NAME);
        binding.setCategoryName(categoryName);
        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.getRemoveCategory().observe(getActivity(), this::closeDialog);
    }

    public void closeDialog(@SuppressWarnings("unused") Void aVoid) {
        dismiss();
    }
}
