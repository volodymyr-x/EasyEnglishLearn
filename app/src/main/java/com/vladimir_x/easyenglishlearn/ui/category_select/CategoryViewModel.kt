package com.vladimir_x.easyenglishlearn.ui.category_select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import com.vladimir_x.easyenglishlearn.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private val _editCategoryLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _removeDialogLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    private val _openCategoryLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val categoriesLiveData = MediatorLiveData<List<String>>()

    val openCategoryLiveData: LiveData<String?>
        get() = _openCategoryLiveData
    val editCategoryLiveData: LiveData<String?>
        get() = _editCategoryLiveData
    val removeDialogLiveData: LiveData<String?>
        get() = _removeDialogLiveData

    init {
        viewModelScope.launch {
            categoriesLiveData.addSource(wordsInteractor.getAllCategories()) {
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

    fun removeCategory(categoryName: String) {
        viewModelScope.launch {
            wordsInteractor.removeCategory(categoryName)
        }
    }
}