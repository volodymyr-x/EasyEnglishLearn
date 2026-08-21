package com.volodymyr_x.easyenglishlearn.ui.category_select

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.navigation.Route

@Composable
fun CategorySelectScreen(action: (Route) -> Unit) {
    val viewModel: CategoryViewModel = hiltViewModel()

    LaunchedEffect(viewModel) {
        viewModel.categoryAction.collect { action ->
            when (action) {
                CategoryAction.CreateNew -> {
                    //callbacks?.onCategoryEdit("")
                    action(Route.CategoryAdd)
                }
                is CategoryAction.Edit -> {
                    //callbacks?.onCategoryEdit(action.categoryName)
                    action(Route.CategoryEdit(categoryName = action.categoryName))
                }
                is CategoryAction.Selected -> {
                    //callbacks?.onCategorySelected(action.categoryName)
                    action(Route.WordSelection(categoryName = action.categoryName))
                }
                is CategoryAction.Removed -> { /*showToast(action.categoryName)*/ }
            }
        }
    }
    val categoryState by viewModel.categoryState.collectAsStateWithLifecycle()
    CategorySelectContent(
        state = categoryState,
        onEvent = { viewModel.onCategoryEvent(it) },
    )
}

@Composable
fun CategorySelectContent(
    state: CategorySelectState,
    onEvent: (CategoryEvent) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(color = Color.Transparent),
        containerColor = Color.Transparent,
        topBar = {
            Text(
                "SELECT CATEGORY",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(CategoryEvent.OnFabClick)
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_action_add),
                    contentDescription = "Add Category"
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                when {
                    state.showDeleteDialog -> CategoryRemoveDialog(
                    categoryName = state.selectedCategoryName,
                        onDismissRequest = { onEvent(CategoryEvent.HideDeleteDialog) },
                        onConfirm = { onEvent(CategoryEvent.OnRemoveClick(state.selectedCategoryName)) }
                )
            }
                LazyColumn {
                    items(state.categoryList.size) { index ->
                        CategoryItem(state.categoryList[index], onEvent)
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(
    categoryName: String,
    onEvent: (CategoryEvent) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { onEvent(CategoryEvent.OnItemClick(categoryName)) }
    ) {
        Text(
            categoryName,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_edit),
            contentDescription = stringResource(id = R.string.ca_cm_edit_category),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onEvent(CategoryEvent.OnEditClick(categoryName)) }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_remove),
            contentDescription = stringResource(id = R.string.ca_cm_remove_category),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onEvent(CategoryEvent.ShowDeleteDialog(categoryName)) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CategorySelectScreenPreview() {
    CategorySelectContent(state = CategorySelectState(
        categoryList = listOf("Category #1", "Category #2", "Category #3")
    ))
}
