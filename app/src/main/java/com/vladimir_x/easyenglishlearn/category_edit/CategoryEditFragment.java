package com.vladimir_x.easyenglishlearn.category_edit;

import androidx.lifecycle.ViewModelProvider;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vladimir_x.easyenglishlearn.R;
import com.vladimir_x.easyenglishlearn.databinding.FragmentCategoryEditBinding;
import com.vladimir_x.easyenglishlearn.ModelFactory;
import com.vladimir_x.easyenglishlearn.databinding.RvCategoryEditItemBinding;
import com.vladimir_x.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vladimir_x.easyenglishlearn.Constants.ARG_CATEGORY_NAME;
import static com.vladimir_x.easyenglishlearn.Constants.EMPTY_STRING;

public class CategoryEditFragment extends Fragment {

    private FragmentCategoryEditBinding mBinding;
    private CategoryEditViewModel mViewModel;
    private CategoryEditAdapter mAdapter;


    @NonNull
    public static Fragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        Fragment fragment = new CategoryEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_category_edit,
                container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String oldCategoryName = requireArguments().getString(ARG_CATEGORY_NAME);
        mViewModel = new ViewModelProvider(this, ModelFactory.getInstance(oldCategoryName))
                .get(CategoryEditViewModel.class);
        mBinding.setViewModel(mViewModel);

        mBinding.cefRvCategoryEdit.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CategoryEditAdapter();
        mBinding.cefRvCategoryEdit.setAdapter(mAdapter);

        int stringId = EMPTY_STRING.equals(oldCategoryName) ?
                R.string.eca_tv_new_category :
                R.string.eca_tv_edit_category;
        mBinding.cefTvTitle.setText(getString(stringId));
        mBinding.cefEtCategoryName.setText(oldCategoryName);

        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
        mViewModel.getWordsLiveData().observe(getViewLifecycleOwner(), mAdapter::setWordList);
        mViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), this::showMessage);
        mViewModel.getFragmentCloseLiveData().observe(getViewLifecycleOwner(), aVoid -> closeFragment());
    }

    private void closeFragment() {
        requireActivity().onBackPressed();
    }

    private void showMessage(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    private class CategoryEditAdapter extends RecyclerView.Adapter<CategoryEditHolder> {

        private List<Word> mWordList = new ArrayList<>();


        @NonNull
        @Override
        public CategoryEditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RvCategoryEditItemBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.rv_category_edit_item,
                    parent,
                    false);
            return new CategoryEditHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryEditHolder categoryEditHolder, int position) {
            categoryEditHolder.bind(mWordList.get(position));
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }

        void setWordList(List<Word> wordList) {
            mWordList = wordList;
            notifyDataSetChanged();
        }
    }

    private class CategoryEditHolder extends RecyclerView.ViewHolder {

        private RvCategoryEditItemBinding mBinding;


        CategoryEditHolder(RvCategoryEditItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(mViewModel);
        }

        void bind(Word word) {
            mBinding.setWord(word);
            mBinding.executePendingBindings();
        }
    }
}