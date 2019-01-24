package com.example.vladimir.easyenglishlearn.category_select;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentCategorySelectBinding;
import com.example.vladimir.easyenglishlearn.databinding.RvCategoryItemBinding;
import com.example.vladimir.easyenglishlearn.fragments.CategoryRemoveFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.ACTION_EDIT_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.ACTION_OPEN_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.CATEGORY_NAME;
import static com.example.vladimir.easyenglishlearn.Constants.DIALOG_REMOVE_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.REQUEST_CATEGORY_REMOVE;

public class CategoryFragment extends Fragment {

    private Callbacks mCallbacks;
    private CategoryViewModel mViewModel;
    private FragmentCategorySelectBinding mBinding;


    /**
     * This interface must be implemented by activities that contain this fragment
     */
    public interface Callbacks {
        void onCategorySelected(String categoryName, int actionCode);
    }

    @NonNull
    public static Fragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_category_select,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rvCategorySelect.setLayoutManager(new LinearLayoutManager(getActivity()));
        CategoryAdapter adapter = new CategoryAdapter();
        mBinding.rvCategorySelect.setAdapter(adapter);

        mBinding.fabCategoryAdd.setOnClickListener(v ->
                mCallbacks.onCategorySelected("", ACTION_EDIT_CATEGORY));

        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mViewModel.getCategoryList()
                .observe(Objects.requireNonNull(getActivity()), adapter::setCategoryList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CATEGORY_REMOVE) {
            String categoryName = data.getStringExtra(CATEGORY_NAME);
            mViewModel.removeCategory(categoryName);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<String> mCategoryList = new ArrayList<>();


        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RvCategoryItemBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.rv_category_item,
                    parent,
                    false);
            return new CategoryHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int position) {
            categoryHolder.bind(mCategoryList.get(position));
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }

        void setCategoryList(List<String> categoryList) {
            mCategoryList = categoryList;
            notifyDataSetChanged();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {

        private String mCategoryName;
        private RvCategoryItemBinding mBinding;


        CategoryHolder(RvCategoryItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

            mBinding.getRoot().setOnClickListener(view ->
                    mCallbacks.onCategorySelected(mCategoryName, ACTION_OPEN_CATEGORY));

            mBinding.categoryEdit.setOnClickListener(view ->
                    mCallbacks.onCategorySelected(mCategoryName, ACTION_EDIT_CATEGORY));

            mBinding.categoryRemove.setOnClickListener(view -> {
                DialogFragment dialogFragment = CategoryRemoveFragment.newInstance(mCategoryName);
                dialogFragment.setTargetFragment(CategoryFragment.this, REQUEST_CATEGORY_REMOVE);
                dialogFragment
                        .show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                                DIALOG_REMOVE_CATEGORY);
            });
        }

        void bind(String categoryName) {
            mCategoryName = categoryName;
            mBinding.setCategoryName(categoryName);
            mBinding.executePendingBindings();
        }
    }
}

