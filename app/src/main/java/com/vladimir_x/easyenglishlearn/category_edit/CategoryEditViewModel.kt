package com.vladimir_x.easyenglishlearn.category_edit

import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.db.WordDao
import com.vladimir_x.easyenglishlearn.model.Word
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CategoryEditViewModel(categoryName: String) : ViewModel() {
    private val _messageLiveData: SingleLiveEvent<Int>
    private val _fragmentCloseLiveData: SingleLiveEvent<Unit>
    private val _wordsLiveData: MutableLiveData<List<Word>>
    private val _currentWordLiveData: MutableLiveData<Pair<String, String>>
    private val repository: WordDao? = App.instance?.database?.wordDao()
    private val disposable: CompositeDisposable
    private val oldCategoryName: String = categoryName
    private var wordIndex = 0
    private var categoryName: String = ""
    private var lexeme: String = ""
    private var translation: String = ""

    val wordsLiveData: LiveData<List<Word>>
        get() = _wordsLiveData

    val currentWordLiveData: LiveData<Pair<String, String>>
        get() = _currentWordLiveData

    val messageLiveData: LiveData<Int?>
        get() = _messageLiveData

    val fragmentCloseLiveData: LiveData<Unit?>
        get() = _fragmentCloseLiveData

    init {
        this.categoryName = categoryName
        _wordsLiveData = MutableLiveData<List<Word>>()
        _currentWordLiveData = MutableLiveData<Pair<String, String>>()
        _messageLiveData = SingleLiveEvent()
        _fragmentCloseLiveData = SingleLiveEvent()
        disposable = CompositeDisposable()
        cleanTextFields()
        subscribeWordsToData()
    }

    fun onBtnSaveCategoryClick(categoryName: String) {
        val newCategoryName: String = categoryName.trim()
        if (TextUtils.isEmpty(newCategoryName)) {
            showMessage(R.string.cef_toast_save_edit_category)
        } else {
            if (TextUtils.isEmpty(oldCategoryName)) {
                _wordsLiveData.value?.let { addNewCategory(newCategoryName, it) }
            } else {
                _wordsLiveData.value?.let { updateCategory(oldCategoryName, newCategoryName, it) }
            }
            _fragmentCloseLiveData.call()
        }
    }

    fun onBtnSaveWordClick(categoryName: String, lexeme: String, translation: String) {
        this.categoryName = categoryName
        this.lexeme = lexeme
        this.translation = translation
        if (isTextFieldsNotEmpty) {
            val newWord = Word(lexeme.trim(), translation.trim())
            _wordsLiveData.value?.let { list ->
                val newList = mutableListOf<Word>().apply {
                    addAll(list)
                }
                if (wordIndex >= 0) {
                    newList[wordIndex] = newWord
                } else {
                    newList.add(newWord)
                }
                _wordsLiveData.value = newList
                cleanTextFields()
            }
        } else {
            showMessage(R.string.cef_toast_save_word_empty_fields)
        }
    }

    fun onBtnCleanClick() {
        cleanTextFields()
    }

    fun onIconRemoveWordClick(word: Word) {
        _wordsLiveData.value?.let { list ->
            val newList = mutableListOf<Word>().apply {
                addAll(list)
            }
            newList.remove(word)
            _wordsLiveData.value = newList
            cleanTextFields()
        }
    }

    fun onItemClick(word: Word) {
        lexeme = word.lexeme
        translation = word.translation
        wordIndex = _wordsLiveData.value?.indexOf(word) ?: -1
        updateCurrentWord()
    }

    private val isTextFieldsNotEmpty: Boolean
        get() = !TextUtils.isEmpty(categoryName.trim()) &&
                !TextUtils.isEmpty(lexeme.trim()) &&
                !TextUtils.isEmpty(translation.trim())

    private fun subscribeWordsToData() {
        val disposable: Disposable? = repository?.getWordsByCategory(oldCategoryName)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe { words: List<Word>? -> _wordsLiveData.postValue(words) }
        disposable?.let { this.disposable.add(it) }
    }

    private fun cleanTextFields() {
        lexeme = Constants.EMPTY_STRING
        translation = Constants.EMPTY_STRING
        wordIndex = -1
        updateCurrentWord()
    }

    private fun showMessage(@StringRes resId: Int) {
        _messageLiveData.value = resId
    }

    private fun addNewCategory(categoryName: String, wordList: List<Word>) {
        val disposable: Disposable = Completable
            .fromAction { repository?.addNewCategory(wordList, categoryName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showMessage(R.string.category_added) }
        this.disposable.add(disposable)
    }

    private fun updateCategory(
        oldCategoryName: String,
        newCategoryName: String,
        wordList: List<Word>
    ) {
        val disposable: Disposable = Completable
            .fromAction {
                repository?.updateCategory(
                    oldCategoryName,
                    newCategoryName,
                    wordList
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showMessage(R.string.category_edited) }
        this.disposable.add(disposable)
    }

    private fun updateCurrentWord() {
        _currentWordLiveData.value = Pair(lexeme, translation)
    }

    override fun onCleared() {
        disposable.dispose()
    }
}