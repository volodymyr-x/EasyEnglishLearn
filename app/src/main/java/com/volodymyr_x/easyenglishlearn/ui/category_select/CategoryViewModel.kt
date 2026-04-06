package com.volodymyr_x.easyenglishlearn.ui.category_select

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volodymyr_x.easyenglishlearn.domain.WordsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val wordsInteractor: WordsInteractor
) : ViewModel() {
    private val _categoryState = MutableStateFlow(CategorySelectState())
    val categoryState: StateFlow<CategorySelectState>
        get() = _categoryState

    private val _categoryAction = Channel<CategoryAction>()
    val categoryAction: Flow<CategoryAction>
        get() = _categoryAction.receiveAsFlow()

    init {
        viewModelScope.launch {
            wordsInteractor.getAllCategories().collect { categories ->
                _categoryState.update { state ->
                    state.copy(categoryList = categories)
                }
            }
        }
    }

    private fun createNewCategory() {
        viewModelScope.launch {
            _categoryAction.send(CategoryAction.Edit(""))
        }
    }

    private fun editCategory(categoryName: String) {
        viewModelScope.launch {
            _categoryAction.send(CategoryAction.Edit(categoryName))
        }
    }

    private fun openCategory(categoryName: String) {
        viewModelScope.launch {
            _categoryAction.send(CategoryAction.Selected(categoryName))
        }
    }

    private fun removeCategory(categoryName: String) {
        _categoryState.update { state ->
            state.copy(showDeleteDialog = false, selectedCategoryName = "")
        }
        viewModelScope.launch {
            wordsInteractor.removeCategory(categoryName)
            _categoryAction.send(CategoryAction.Removed(categoryName))
        }
    }

    private fun showDeleteDialog(categoryName: String) {
        _categoryState.update { state ->
            state.copy(showDeleteDialog = true, selectedCategoryName = categoryName)
        }
    }

    private fun hideDeleteDialog() {
        _categoryState.update { state ->
            state.copy(showDeleteDialog = false, selectedCategoryName = "")
        }
    }

    fun onCategoryEvent(event: CategoryEvent) {
        when(event) {
            is CategoryEvent.OnEditClick -> editCategory(event.categoryName)
            is CategoryEvent.OnRemoveClick -> removeCategory(event.categoryName)
            is CategoryEvent.OnItemClick -> openCategory(event.categoryName)
            is CategoryEvent.OnFabClick -> createNewCategory()
            is CategoryEvent.ShowDeleteDialog -> showDeleteDialog(event.categoryName)
            CategoryEvent.HideDeleteDialog -> hideDeleteDialog()
        }
    }
}
