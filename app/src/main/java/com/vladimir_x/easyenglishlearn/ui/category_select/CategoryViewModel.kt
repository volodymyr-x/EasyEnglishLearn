package com.vladimir_x.easyenglishlearn.ui.category_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimir_x.easyenglishlearn.Constants
import com.vladimir_x.easyenglishlearn.domain.WordsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    val categories: Flow<List<String>> = wordsInteractor.getAllCategories()
    private val _categoryState =
        MutableStateFlow<CategorySelectState>(CategorySelectState.IdleState)
    val categoryState: StateFlow<CategorySelectState>
        get() = _categoryState

    fun onFabClick() {
        changeCategoryState(CategorySelectState.EditCategory(Constants.EMPTY_STRING))
    }

    fun onRemoveClick(categoryName: String) {
        changeCategoryState(CategorySelectState.RemoveCategory(categoryName))
    }

    fun onEditClick(categoryName: String) {
        changeCategoryState(CategorySelectState.EditCategory(categoryName))
    }

    fun onItemClick(categoryName: String) {
        changeCategoryState(CategorySelectState.OpenCategory(categoryName))
    }

    fun removeCategory(categoryName: String) {
        viewModelScope.launch {
            wordsInteractor.removeCategory(categoryName)
        }
    }

    private fun changeCategoryState(state: CategorySelectState) {
        _categoryState.value = state
        _categoryState.value = CategorySelectState.IdleState
    }
}