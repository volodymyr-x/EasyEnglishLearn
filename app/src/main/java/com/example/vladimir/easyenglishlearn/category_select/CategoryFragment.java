package com.example.vladimir.easyenglishlearn.category_select;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentCategorySelectBinding;
import com.example.vladimir.easyenglishlearn.databinding.RvCategoryItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.DIALOG_REMOVE_CATEGORY;

public class CategoryFragment extends Fragment {

    private Callbacks mCallbacks;
    private CategoryViewModel mViewModel;
    private FragmentCategorySelectBinding mBinding;
    private CategoryAdapter mAdapter;


    /**
     * This interface must be implemented by activities that contain this fragment
     */
    public interface Callbacks {
        void onCategorySelected(String categoryName);

        void onCategoryEdit(String categoryName);
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_category_select,
                container,
                false);
        mBinding.rvCategorySelect.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mBinding.fabCategoryAdd.getVisibility() == View.VISIBLE) {
                    mBinding.fabCategoryAdd.hide();
                } else if (dy < 0 && mBinding.fabCategoryAdd.getVisibility() != View.VISIBLE) {
                    mBinding.fabCategoryAdd.show();
                }
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        mBinding.setViewModel(mViewModel);

        mBinding.rvCategorySelect.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CategoryAdapter();
        mBinding.rvCategorySelect.setAdapter(mAdapter);
        subscribeToLiveData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private void subscribeToLiveData() {
        mViewModel.getCategoriesLiveData().observe(this, mAdapter::setCategoryList);
        mViewModel.getEditCategoryLiveData().observe(this, mCallbacks::onCategoryEdit);
        mViewModel.getRemoveDialogLiveData().observe(this, this::showDialog);
        mViewModel.getOpenCategoryLiveData().observe(this, mCallbacks::onCategorySelected);
        mViewModel.getMessageLiveData().observe(this, this::showMessage);
    }

    public void showMessage(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    private void showDialog(String categoryName) {
        DialogFragment dialogFragment = CategoryRemoveFragment.newInstance(categoryName);
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                DIALOG_REMOVE_CATEGORY);
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

        private RvCategoryItemBinding mBinding;


        CategoryHolder(RvCategoryItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(mViewModel);
        }

        void bind(String categoryName) {
            mBinding.setCategoryName(categoryName);
            mBinding.executePendingBindings();
        }
    }
}

