package com.example.vladimir.easyenglishlearn.category_edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.vladimir.easyenglishlearn.Constants.ARG_CATEGORY_NAME;

public class CategoryEditFragment extends Fragment {

    @BindView(R.id.cef_tv_title)
    TextView tvTitle;
    @BindView(R.id.cef_et_category_name)
    EditText etCategoryName;
    @BindView(R.id.cef_et_lexeme)
    EditText etEditLexeme;
    @BindView(R.id.cef_et_translation)
    EditText etEditTranslation;
    @BindView(R.id.cef_btn_save_word)
    Button btnSaveWord;
    @BindView(R.id.cef_rv_category_edit)
    RecyclerView mRecyclerView;
    private String mOldCategoryName;
    private CategoryRepository mRepository;
    private List<Word> mWordList;
    private int wordIndex = -1;


    public static Fragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        Fragment fragment = new CategoryEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_edit, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepository = CategoryRepositoryImpl.getInstance();

        mOldCategoryName = Objects.requireNonNull(getArguments()).getString(ARG_CATEGORY_NAME);
        if (mOldCategoryName != null) {
            tvTitle.setText(getString(R.string.eca_tv_edit_category));
            etCategoryName.setText(mOldCategoryName);
            loadWords(mOldCategoryName);
        } else {
            tvTitle.setText(getString(R.string.eca_tv_new_category));
            mWordList = new ArrayList<>();
        }
        updateUI();
        return view;
    }

    @OnClick(R.id.cef_btn_save_category)
    public void onBtnSaveCategoryClick() {
        String mNewCategoryName = etCategoryName.getText().toString();
        if (!TextUtils.isEmpty(mNewCategoryName)) {
            if (mOldCategoryName != null) {
                mRepository.updateCategory(mOldCategoryName, mNewCategoryName, mWordList);
            } else {
                mRepository.addNewCategory(mNewCategoryName, mWordList);
            }
            Objects.requireNonNull(getActivity()).onBackPressed();
        } else {
            showToast(R.string.cef_toast_save_edit_category);
        }
    }

    @OnClick(R.id.cef_btn_save_word)
    public void onBtnSaveWordClick() {
        if (isTextFieldsNotEmpty()) {
            Word newWord = new Word(etEditLexeme.getText().toString().trim(),
                    etEditTranslation.getText().toString().trim());
            if (wordIndex >= 0) {
                mWordList.set(wordIndex, newWord);
            } else {
                mWordList.add(newWord);
            }
            updateUI();
            btnSaveWord.setText(getString(R.string.cef_save_word));
            cleanTextFields();
        } else {
            showToast(R.string.cef_toast_save_word_empty_fields);
        }
    }

    @OnClick(R.id.cef_btn_clean)
    public void onBtnCleanClick() {
        btnSaveWord.setText(getString(R.string.cef_category_edit_clean));
        cleanTextFields();
    }

    private boolean isTextFieldsNotEmpty() {
        return !TextUtils.isEmpty(etCategoryName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etEditLexeme.getText().toString().trim()) &&
                !TextUtils.isEmpty(etEditTranslation.getText().toString().trim());
    }

    public void cleanTextFields() {
        etEditLexeme.setText("");
        etEditTranslation.setText("");
        wordIndex = -1;
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    private void updateUI() {
        mRecyclerView.setAdapter(new CategoryEditAdapter(mWordList));
    }

    private void loadWords(String categoryName) {
        mWordList = mRepository.getWordsByCategory(categoryName);
    }

    private class CategoryEditAdapter extends RecyclerView.Adapter<CategoryEditHolder> {

        private List<Word> mWordList;


        CategoryEditAdapter(List<Word> wordList) {
            mWordList = wordList;
        }

        @NonNull
        @Override
        public CategoryEditHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new CategoryEditHolder(LayoutInflater.from(getActivity()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryEditHolder categoryEditHolder, int position) {
            categoryEditHolder.bind(mWordList.get(position));
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }
    }

    private class CategoryEditHolder extends RecyclerView.ViewHolder {

        private TextView mLexeme;
        private TextView mTranslation;
        private Word mWord;


        CategoryEditHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.rv_category_edit_item, parent, false));
            mLexeme = itemView.findViewById(R.id.cei_tv_lexeme);
            mTranslation = itemView.findViewById(R.id.cei_tv_translation);
            itemView.setOnClickListener(view -> {
                etEditLexeme.setText(mWord.getLexeme());
                etEditTranslation.setText(mWord.getTranslation());
                wordIndex = mWordList.indexOf(mWord);
            });
            itemView.findViewById(R.id.cei_iv_word_remove).setOnClickListener(v -> {
                mWordList.remove(mWord);
                updateUI();
                cleanTextFields();
            });
        }

        void bind(Word word) {
            mWord = word;
            mLexeme.setText(mWord.getLexeme());
            mTranslation.setText(mWord.getTranslation());
        }
    }
}