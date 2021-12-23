package com.vladimir_x.easyenglishlearn.category_select

import androidx.lifecycle.ViewModel
import com.vladimir_x.easyenglishlearn.db.WordDao
import androidx.lifecycle.LiveData
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.Constants

class CategoryViewModel : ViewModel() {
    private val repository: WordDao?
    val categoriesLiveData: LiveData<List<String>>?
    private val _editCategoryLiveData: SingleLiveEvent<String>
    private val _removeDialogLiveData: SingleLiveEvent<String>
    private val _openCategoryLiveData: SingleLiveEvent<String>
    private val _removeCategoryLiveData: SingleLiveEvent<Unit>
    private val _messageLiveData: SingleLiveEvent<Int>
    private val disposable: CompositeDisposable

    val openCategoryLiveData: LiveData<String?>
        get() = _openCategoryLiveData
    val editCategoryLiveData: LiveData<String?>
        get() = _editCategoryLiveData
    val removeDialogLiveData: LiveData<String?>
        get() = _removeDialogLiveData
    val removeCategoryLiveData: LiveData<Unit?>
        get() = _removeCategoryLiveData
    val messageLiveData: LiveData<Int?>
        get() = _messageLiveData

    init {
        repository = App.instance?.database?.wordDao()
        categoriesLiveData = repository?.getAllCategories()
        _editCategoryLiveData = SingleLiveEvent()
        _removeDialogLiveData = SingleLiveEvent()
        _openCategoryLiveData = SingleLiveEvent()
        _removeCategoryLiveData = SingleLiveEvent()
        _messageLiveData = SingleLiveEvent()
        disposable = CompositeDisposable()
    }

    fun onFabClick() {
        _editCategoryLiveData.value = Constants.EMPTY_STRING
    }

    fun onRemoveClick(categoryName: String?) {
        _removeDialogLiveData.value = categoryName
    }

    fun onEditClick(categoryName: String?) {
        _editCategoryLiveData.value = categoryName
    }

    fun onItemClick(categoryName: String?) {
        _openCategoryLiveData.value = categoryName
    }

    fun removeCategory(categoryName: String?) {
        val disposable = Completable
            .fromAction { repository?.removeCategory(categoryName) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showMessage() }
        _removeCategoryLiveData.call()
        this.disposable.add(disposable)
    }

    fun cancelRemoving() {
        _removeCategoryLiveData.call()
    }

    private fun showMessage() {
        _messageLiveData.value = R.string.category_removed
    }

    override fun onCleared() {
        disposable.dispose()
    }
}