package com.example.vladimir.easyenglishlearn.db;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CategoryRepositoryImpl implements CategoryRepository {

    private static CategoryRepositoryImpl repository;
    private static List<Word> sWordList = new ArrayList<>();
    private MutableLiveData<List<String>> mData;

    static {
        sWordList.add(new Word("milk", "молоко", "Еда и Напитки"));
        sWordList.add(new Word("apple", "яблоко", "Еда и Напитки"));
        sWordList.add(new Word("bread", "хлеб", "Еда и Напитки"));
        sWordList.add(new Word("butter", "масло", "Еда и Напитки"));

        sWordList.add(new Word("football", "футбол", "Спорт"));
        sWordList.add(new Word("tennis", "тенис", "Спорт"));
        sWordList.add(new Word("basketball", "баскетбол", "Спорт"));
        sWordList.add(new Word("snooker", "снукер", "Спорт"));

        sWordList.add(new Word("money", "деньги", "Поход в магазин"));
        sWordList.add(new Word("queue", "очередь", "Поход в магазин"));
        sWordList.add(new Word("seller", "продавец", "Поход в магазин"));
    }

    private CategoryRepositoryImpl() {
    }

    public static CategoryRepository getInstance() {
        if (repository == null) {
            repository = new CategoryRepositoryImpl();
        }
        return repository;
    }

    @Override
    public List<Word> getWordsByCategory(String categoryName) {
        List<Word> words = new ArrayList<>();
        for (Word word : sWordList) {
            if (word.getCategory().equals(categoryName)) {
                words.add(word);
            }
        }
        return words;
    }

    @Override
    public MutableLiveData<List<String>> getAllCategories() {
        Set<String> set = new HashSet<>();
        for (Word word : sWordList) {
            set.add(word.getCategory());
        }
        if (mData == null) mData = new MutableLiveData<>();
        mData.postValue(new ArrayList<>(set));
        return mData;
    }

    @Override
    public void addNewCategory(String categoryName, List<Word> wordList) {
        for (Word word : wordList) {
            word.setCategory(categoryName);
        }
        new AddNewCategoryAsyncTask(this).execute(wordList);
    }

    @Override
    public void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        removeCategory(oldCategoryName);
        addNewCategory(newCategoryName, wordList);
    }

    @Override
    public void removeCategory(String categoryName) {
        new RemoveCategoryAsyncTask(this).execute(categoryName);
    }

    private static class AddNewCategoryAsyncTask extends AsyncTask<List<Word>, Void, Void> {

        CategoryRepositoryImpl mRepository;


        AddNewCategoryAsyncTask(CategoryRepositoryImpl repository) {
            mRepository = repository;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Word>... lists) {
            sWordList.addAll(lists[0]);
            repository.getAllCategories();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRepository = null;
        }
    }

    private static class RemoveCategoryAsyncTask extends AsyncTask<String, Void, Void> {

        CategoryRepositoryImpl mRepository;


        RemoveCategoryAsyncTask(CategoryRepositoryImpl repository) {
            mRepository = repository;
        }

        @Override
        protected Void doInBackground(String... strings) {
            String categoryName = strings[0];
            for (Iterator<Word> iterator = sWordList.iterator(); iterator.hasNext(); ) {
                Word word = iterator.next();
                if (word.getCategory().equals(categoryName)) iterator.remove();
            }
            repository.getAllCategories();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRepository = null;
        }
    }
}
