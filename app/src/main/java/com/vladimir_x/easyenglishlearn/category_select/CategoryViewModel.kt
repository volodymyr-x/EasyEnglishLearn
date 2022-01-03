package com.vladimir_x.easyenglishlearn.category_select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.App
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.R
import com.vladimir_x.easyenglishlearn.SingleLiveEvent
import com.vladimir_x.easyenglishlearn.db.WordDao
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val repository: WordDao? = App.instance?.database?.wordDao()
    private val _editCategoryLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _removeDialogLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _openCategoryLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _removeCategoryLiveData: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _messageLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    val categoriesLiveData = MediatorLiveData<List<String>>()

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
        viewModelScope.launch {
            categoriesLiveData.addSource(repository!!.getAllCategories()) {
                categoriesLiveData.value = it
            }
        }
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
        viewModelScope.launch {
            repository?.removeCategory(categoryName)
            showMessage()
            _removeCategoryLiveData.call()
        }
    }

    fun cancelRemoving() {
        _removeCategoryLiveData.call()
    }

    private fun showMessage() {
        _messageLiveData.value = R.string.category_removed
    }
}