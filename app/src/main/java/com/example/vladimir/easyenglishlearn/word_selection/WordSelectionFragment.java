package com.example.vladimir.easyenglishlearn.word_selection;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
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

import com.example.vladimir.easyenglishlearn.ExerciseActivity;
import com.example.vladimir.easyenglishlearn.ModelFactory;
import com.example.vladimir.easyenglishlearn.R;
import com.example.vladimir.easyenglishlearn.databinding.FragmentWordSelectionBinding;
import com.example.vladimir.easyenglishlearn.databinding.RvWordSelectionItemBinding;
import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.vladimir.easyenglishlearn.Constants.ANSWERS_COUNT;
import static com.example.vladimir.easyenglishlearn.Constants.ARG_CATEGORY_NAME;
import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_CHOICE_FRAGMENT;
import static com.example.vladimir.easyenglishlearn.Constants.EXERCISE_TYPE;
import static com.example.vladimir.easyenglishlearn.Constants.SELECTED_WORDS;
import static com.example.vladimir.easyenglishlearn.Constants.TRANSLATION_DIRECTION;

public class WordSelectionFragment extends Fragment {

    private FragmentWordSelectionBinding mBinding;
    private WordSelectionAdapter mAdapter;
    private WordSelectionViewModel mViewModel;


    @NonNull
    public static Fragment newInstance(String categoryName) {
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        Fragment fragment = new WordSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_word_selection,
                container,
                false);
        mBinding.wsfRvWordsChoice.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String categoryName = Objects.requireNonNull(getArguments()).getString(ARG_CATEGORY_NAME);
        mAdapter = new WordSelectionAdapter();
        mBinding.wsfRvWordsChoice.setAdapter(mAdapter);

        mViewModel = ViewModelProviders
                .of(this, ModelFactory.getInstance(categoryName))
                .get(WordSelectionViewModel.class);
        mBinding.setViewModel(mViewModel);
        subscribeToLiveData();
    }

    public void showMessage(@StringRes int resId) {
        String message = getString(resId, ANSWERS_COUNT);
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void subscribeToLiveData() {
        mViewModel.getWordsLiveData().observe(this, mAdapter::setWordList);
        mViewModel.getMessageLiveData().observe(this, this::showMessage);
        mViewModel.getChoiceDialogLiveData().observe(this, this::showDialog);
        mViewModel.getSelectedWordsLiveData().observe(this, this::startExercise);
    }

    private void showDialog(String categoryName) {
        DialogFragment dialogFragment = ExerciseChoiceFragment.newInstance(categoryName);
        dialogFragment.show(Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager(), EXERCISE_CHOICE_FRAGMENT);
    }

    private void startExercise(WordSelectionDto dto) {
        Intent intent = new Intent(getActivity(), ExerciseActivity.class);
        intent.putExtra(EXERCISE_TYPE, dto.getExercise());
        intent.putParcelableArrayListExtra(SELECTED_WORDS, dto.getSelectedWordList());
        intent.putExtra(TRANSLATION_DIRECTION, dto.isTranslationDirection());
        startActivity(intent);
    }

    private class WordSelectionAdapter extends RecyclerView.Adapter<WordSelectionHolder> {

        private List<Word> mWordList = new ArrayList<>();


        @NonNull
        @Override
        public WordSelectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RvWordSelectionItemBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.rv_word_selection_item,
                    parent,
                    false);
            return new WordSelectionHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull WordSelectionHolder wordSelectionHolder, int position) {
            wordSelectionHolder.bind(mWordList.get(position));
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

    private class WordSelectionHolder extends RecyclerView.ViewHolder {

        private RvWordSelectionItemBinding mBinding;


        WordSelectionHolder(RvWordSelectionItemBinding binding) {
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

