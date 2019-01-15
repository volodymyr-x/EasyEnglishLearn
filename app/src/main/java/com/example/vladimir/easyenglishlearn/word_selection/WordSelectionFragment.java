package com.example.vladimir.easyenglishlearn.word_selection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vladimir.easyenglishlearn.Constants.Exercises;
import com.example.vladimir.easyenglishlearn.ExerciseActivity;
import com.example.vladimir.easyenglishlearn.db.CategoryRepository;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.db.CategoryRepositoryImpl;
import com.example.vladimir.easyenglishlearn.fragments.ExerciseChoiceFragment;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.example.vladimir.easyenglishlearn.Constants.ARG_CATEGORY_NAME;
import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_CHOICE_FRAGMENT;
import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_TYPE;
import static com.example.vladimir.easyenglishlearn.Constants.REQUEST_EXERCISE_CHOICE;
import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_CONSTRUCTOR;
import static com.example.vladimir.easyenglishlearn.Constants.WORD_QUIZ;

public class WordSelectionFragment extends Fragment {

    @BindView(R.id.wsf_tv_category_name)
    TextView tvCategoryName;
    @BindView(R.id.wsf_rv_words_choice)
    RecyclerView mRecyclerView;
    private List<Word> mWordList;
    private ArrayList<Word> mSelectedWordList;
    private CategoryRepository mRepository;
    private WordSelectionAdapter mAdapter;
    private String mCategoryName;
    private boolean isSelectedAll;


    public static Fragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        Fragment fragment = new WordSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_selection, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRepository = CategoryRepositoryImpl.getInstance();
        mCategoryName = Objects.requireNonNull(getArguments()).getString(ARG_CATEGORY_NAME);
        tvCategoryName.setText(mCategoryName);
        mSelectedWordList = new ArrayList<>();
        updateUI();
        return view;
    }

    @OnClick(R.id.wsf_btn_start)
    public void onButtonStartClick() {
        if (mSelectedWordList.size() < 4) {
            showToast(R.string.wsa_toast_min_words_count);
        } else {
            DialogFragment dialogFragment = ExerciseChoiceFragment.newInstance();
            dialogFragment.setTargetFragment(WordSelectionFragment.this, REQUEST_EXERCISE_CHOICE);
            dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                    EXERCISE_CHOICE_FRAGMENT);
        }
    }

    @OnCheckedChanged(R.id.wsf_cb_choose_all)
    void onCheckBoxCheckedChange(CompoundButton button, boolean checked) {
        isSelectedAll = checked;
        if (!isSelectedAll) mSelectedWordList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    private Intent getPreparedIntent(@Exercises String exerciseType, boolean translationDirection) {
        Intent intent = new Intent(getActivity(), ExerciseActivity.class);
        intent.putExtra(EXERCISE_TYPE, exerciseType);
        intent.putParcelableArrayListExtra(SELECTED_WORDS, mSelectedWordList);
        intent.putExtra(TRANSLATION_DIRECTION, translationDirection);
        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_EXERCISE_CHOICE) {
            boolean translationDirection = data.getBooleanExtra(TRANSLATION_DIRECTION, true);
            String exerciseType = data.getStringExtra(EXERCISE_TYPE);
            switch (exerciseType) {
                case WORD_CONSTRUCTOR:
                    startActivity(getPreparedIntent(WORD_CONSTRUCTOR, translationDirection));
                    break;
                case WORD_QUIZ:
                    startActivity(getPreparedIntent(WORD_QUIZ, translationDirection));
                    break;
            }
        }
    }

    private void updateUI() {
        //if (mAdapter == null) {
        mWordList = mRepository.getWordsByCategory(mCategoryName);
        mAdapter = new WordSelectionAdapter(mWordList);
        mRecyclerView.setAdapter(mAdapter);
        //} else {
        //    mAdapter.setWordList(mWordList);
        //    mAdapter.notifyDataSetChanged();
        //}
    }

    private void loadWords(String categoryName) {
        mWordList = mRepository.getWordsByCategory(categoryName);
    }

    private class WordSelectionAdapter extends RecyclerView.Adapter<WordSelectionHolder> {

        private List<Word> mWordList;


        WordSelectionAdapter(List<Word> wordList) {
            mWordList = wordList;
        }

        @NonNull
        @Override
        public WordSelectionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            return new WordSelectionHolder(LayoutInflater.from(getActivity()), viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull WordSelectionHolder wordSelectionHolder, int position) {
            wordSelectionHolder.bind(mWordList.get(position));
        }

        @Override
        public int getItemCount() {
            return mWordList.size();
        }

        public void setWordList(List<Word> wordList) {
            mWordList = wordList;
        }
    }

    private class WordSelectionHolder extends RecyclerView.ViewHolder {

        private TextView mLexeme;
        private TextView mTranslation;
        private CheckBox mCheckBox;
        private Word mWord;


        WordSelectionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.rv_word_selection_item, parent, false));
            mLexeme = itemView.findViewById(R.id.wsi_tv_lexeme);
            mTranslation = itemView.findViewById(R.id.wsi_tv_translation);
            mCheckBox = itemView.findViewById(R.id.wsi_cb_word_choice);
            mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!mSelectedWordList.contains(mWord)) mSelectedWordList.add(mWord);
                } else {
                    mSelectedWordList.remove(mWord);
                }
            });
        }

        void bind(Word word) {
            mWord = word;
            mLexeme.setText(mWord.getLexeme());
            mTranslation.setText(mWord.getTranslation());
            mCheckBox.setChecked(isSelectedAll || mSelectedWordList.contains(mWord));
        }
    }
}

