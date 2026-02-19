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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.R

@Composable
fun CategorySelectContent(
    categoryList: List<String>,
    categoryClickAction: (CategoryAction) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(color = Color.Transparent),
        containerColor = Color.Transparent,
        topBar = {
            Text(
                "SELECT CATEGORY",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    categoryClickAction(CategoryAction.CreateNew)
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
                LazyColumn {
                    items(categoryList.size) { index ->
                        CategoryItem(categoryList[index], categoryClickAction)
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(
    categoryName: String,
    clickAction: (CategoryAction) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { clickAction(CategoryAction.Selected(categoryName)) }
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
                .clickable { clickAction(CategoryAction.Edit(categoryName)) }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_remove),
            contentDescription = stringResource(id = R.string.ca_cm_remove_category),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { clickAction(CategoryAction.Remove(categoryName)) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CategorySelectScreenPreview() {
    CategorySelectContent(categoryList = listOf("Category #1", "Category #2", "Category #3"))
}
