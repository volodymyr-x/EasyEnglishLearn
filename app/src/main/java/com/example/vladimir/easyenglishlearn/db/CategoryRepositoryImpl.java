package com.example.vladimir.easyenglishlearn.db;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.vladimir.easyenglishlearn.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoryRepositoryImpl implements CategoryRepository {

    //private Map<String, DataChangeListener> mListenerList = new HashMap<>();
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
        /*return new ArrayList<>(set);*/

        /*try {
            return new getAllCategoriesAsyncTask().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new MutableLiveData<>();*/
        if (mData == null) mData = new MutableLiveData<>();
        mData.postValue(new ArrayList<>(set));
        return mData;
    }

    @Override
    public void addNewCategory(String categoryName, List<Word> wordList) {
        for (Word word : wordList) {
            word.setCategory(categoryName);
            sWordList.add(word);
        }
        //notifyListeners();
        getAllCategories();
    }

    @Override
    public void updateCategory(String oldCategoryName, String newCategoryName, List<Word> wordList) {
        removeCategory(oldCategoryName);
        addNewCategory(newCategoryName, wordList);
    }

    @Override
    public void removeCategory(String categoryName) {
        for (Iterator<Word> iterator = sWordList.iterator(); iterator.hasNext(); ) {
            Word word = iterator.next();
            if (word.getCategory().equals(categoryName)) iterator.remove();
        }
        //notifyListeners();
        getAllCategories();
    }

//    @Override
//    public void setDataChangeListener(String key, DataChangeListener listener) {
//        mListenerList.put(key, listener);
//    }

//    @Override
//    public void removeDataChangeListener(String key) {
//        mListenerList.remove(key);
//    }

//    private void notifyListeners() {
//        for (DataChangeListener listener : mListenerList.values()) {
//            listener.dataIsChanged();
//        }
//    }

    private static class getAllCategoriesAsyncTask
            extends AsyncTask<Void, Void, MutableLiveData<List<String>>> {

        @Override
        protected MutableLiveData<List<String>> doInBackground(Void... voids) {
            Set<String> set = new HashSet<>();
            for (Word word : sWordList) {
                set.add(word.getCategory());
            }
            MutableLiveData<List<String>> data = new MutableLiveData<>();
            data.postValue(new ArrayList<>(set));
            return data;
        }
    }
}
