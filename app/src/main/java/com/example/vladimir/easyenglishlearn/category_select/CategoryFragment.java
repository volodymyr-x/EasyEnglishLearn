package com.example.vladimir.easyenglishlearn.category_select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.fragments.CategoryRemoveFragment;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;

import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.ACTION_EDIT_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.ACTION_OPEN_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.CATEGORY_LISTENER;
import static com.example.vladimir.easyenglishlearn.Constants.CATEGORY_NAME;
import static com.example.vladimir.easyenglishlearn.Constants.DIALOG_REMOVE_CATEGORY;
import static com.example.vladimir.easyenglishlearn.Constants.REQUEST_CATEGORY_REMOVE;

public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CategoryRepository mRepository;
    private Callbacks mCallbacks;


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
        View view = inflater.inflate(R.layout.fragment_category_select, container, false);
        mRecyclerView = view.findViewById(R.id.rv_category_select);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = view.findViewById(R.id.fab_category_add);
        fab.setOnClickListener(v -> mCallbacks.onCategorySelected(null, ACTION_EDIT_CATEGORY));

        mRepository = CategoryRepositoryImpl.getInstance();
        mRepository.setDataChangeListener(CATEGORY_LISTENER, this::updateUI);

        updateUI();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRepository.removeDataChangeListener(CATEGORY_LISTENER);
        mCallbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CATEGORY_REMOVE) {
            String categoryName = data.getStringExtra(CATEGORY_NAME);
            mRepository.removeCategory(categoryName);
        }
    }

    private void updateUI() {
        List<String> categoryList = mRepository.getAllCategories();
        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        mRecyclerView.setAdapter(adapter);
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<String> mCategoryList;


        CategoryAdapter(List<String> categoryList) {
            mCategoryList = categoryList;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new CategoryHolder(LayoutInflater.from(getActivity()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder categoryHolder, int position) {
            categoryHolder.bind(mCategoryList.get(position));
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {

        private TextView mCategoryName;
        private String mCategory;


        CategoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.rv_category_item, parent, false));
            mCategoryName = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(view ->
                    mCallbacks.onCategorySelected(mCategory, ACTION_OPEN_CATEGORY));

            itemView.findViewById(R.id.category_edit).setOnClickListener(view ->
                    mCallbacks.onCategorySelected(mCategory, ACTION_EDIT_CATEGORY));

            itemView.findViewById(R.id.category_remove).setOnClickListener(view -> {
                DialogFragment dialogFragment = CategoryRemoveFragment.newInstance(mCategory);
                dialogFragment.setTargetFragment(CategoryFragment.this, REQUEST_CATEGORY_REMOVE);
                dialogFragment
                        .show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                                DIALOG_REMOVE_CATEGORY);
            });
        }

        void bind(String category) {
            mCategory = category;
            mCategoryName.setText(mCategory);
        }
    }
}

