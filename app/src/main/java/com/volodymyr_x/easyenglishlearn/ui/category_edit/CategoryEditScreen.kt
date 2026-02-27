package com.volodymyr_x.easyenglishlearn.ui.category_edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.model.Word
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun CategoryEditContent(
    categoryName: String,
    categoryWords: List<Word>,
    action: (CategoryEditAction) -> Unit = {}
) {
    var currentCategoryName by remember { mutableStateOf(categoryName) }
    var currentLexeme by remember { mutableStateOf("") }
    var currentTranslation by remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(color = Color.Transparent),
        containerColor = Color.Transparent,
        topBar = {
            Text(
                text = stringResource(id = getCorrectTitle(categoryName)),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            )
            {
                OutlinedTextField(
                    value = currentCategoryName,
                    onValueChange = { currentCategoryName = it },
                    label = { Text("Category name") },
                    modifier = Modifier.fillMaxWidth()
                )

                VerticalSpacer()

                Button(
                    onClick = { action(CategoryEditAction.SaveCategory(currentCategoryName)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.eca_btn_save_and_exit),
                        fontSize = 18.sp
                    )
                }

                VerticalSpacer()

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = currentLexeme,
                        onValueChange = { currentLexeme = it },
                        label = { Text(stringResource(R.string.eca_tv_lexeme)) },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = currentTranslation,
                        onValueChange = { currentTranslation = it },
                        label = { Text(stringResource(R.string.eca_tv_translation)) },
                        modifier = Modifier.weight(1f)
                    )
                }

                VerticalSpacer()

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            if (currentLexeme.isNotBlank() && currentTranslation.isNotBlank()) {
                                action(
                                    CategoryEditAction.AddWord(
                                        categoryName = currentCategoryName,
                                        lexeme = currentLexeme,
                                        translation = currentTranslation
                                    )
                                )
                                currentLexeme = ""
                                currentTranslation = ""
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(R.string.eca_btn_save_word),
                            fontSize = 18.sp
                        )
                    }

                    Button(
                        onClick = { currentLexeme = ""; currentTranslation = "" },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(R.string.eca_btn_clean),
                            fontSize = 18.sp
                        )
                    }
                }

                VerticalSpacer()

                LazyColumn {
                    items(categoryWords.size) { index ->
                        WordItem(
                            categoryWords[index],
                            clickAction = { word ->
                                currentLexeme = word.lexeme
                                currentTranslation = word.translation
                            },
                            deleteAction = { word ->
                                action(CategoryEditAction.RemoveWord(word))
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun WordItem(
    word: Word,
    clickAction: (Word) -> Unit = {},
    deleteAction: (Word) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable { clickAction(word) }
    ) {
        Text(
            word.lexeme,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            word.translation,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_remove),
            contentDescription = stringResource(R.string.ca_cm_remove_word),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { deleteAction(word) }
        )
    }
}

private fun getCorrectTitle(categoryName: String): Int =
    if (categoryName.isEmpty()) {
        R.string.eca_tv_new_category
    } else {
        R.string.eca_tv_edit_category
    }

@Preview(showSystemUi = true)
@Composable
fun CategoryEditContentPreview() {
    CategoryEditContent(
        categoryName = "Category name",
        categoryWords = listOf(
            Word("Lexeme #1", "Translation №1"),
            Word("Lexeme #2", "Translation №2"),
        )
    )
}
