package com.volodymyr_x.easyenglishlearn.ui.word_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer
import com.volodymyr_x.easyenglishlearn.ui.model.WordUI

@Composable
fun WordSelectionScreen(
    categoryName: String,
    startExerciseAction: (WordSelectionResult) -> Unit,
    showMessageAction: (String) -> Unit = {}
) {
    val viewModel = hiltViewModel { factory: WordSelectionViewModel.Factory ->
        factory.create(categoryName)
    }
    val message =
        stringResource(R.string.wsa_toast_min_words_count, Constants.MIN_CHECKED_WORD_QUANTITY)
    LaunchedEffect(viewModel) {
        viewModel.wordSelectionAction.collect { action ->
            when (action) {
                WordSelectionAction.ShowMessage -> showMessageAction(message)
                is WordSelectionAction.StartExercise -> startExerciseAction(action.result)
            }
        }
    }
    val wordSelectionState =
        viewModel.screenState.collectAsStateWithLifecycle().value
    WordSelectionContent(
        state = wordSelectionState,
        action = viewModel::onAction
    )
}

@Composable
fun WordSelectionContent(
    state: WordSelectionState,
    action: (WordSelectionEvent) -> Unit = {}
) {
    var interactionSource by remember { mutableStateOf(MutableInteractionSource()) }
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .background(color = Color.Transparent),
        containerColor = Color.Transparent,
        topBar = {
            Text(
                text = stringResource(R.string.wsa_tv_choose_words),
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
                when {
                    state.openChooseExerciseDialog -> ExerciseChoiceContent(
                        {
                            action(WordSelectionEvent.HideDialog)
                        },
                        { exerciseChoiceDto ->
                            action(WordSelectionEvent.OnExerciseChoose(exerciseChoiceDto))
                        }
                    )
                }
                Text(
                    text = state.categoryName,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )

                VerticalSpacer()

                Button(
                    onClick = {
                        action(WordSelectionEvent.OnBtnStartClick)
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.wsa_btn_start))
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            role = Role.Checkbox,
                            onClick = {
                                action(WordSelectionEvent.OnChooseAllClick)
                            },
                            indication = ripple(color = Color.Blue),
                            interactionSource = interactionSource,
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.wsa_cb_choose_all),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Checkbox(
                        checked = state.isChooseAllChecked,
                        onCheckedChange = null // Set to null as the parent Row handles the click
                    )
                }

                VerticalSpacer()
                LazyColumn {
                    items(state.categoryWords.size) { index ->
                        val word = state.categoryWords[index]
                        WordSelectionItem(
                            word,
                            onChecked = { action(WordSelectionEvent.OnItemCheckBoxChange(word)) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun WordSelectionItem(
    word: WordUI,
    onChecked: (WordUI) -> Unit = {}
) {
    var interactionSource by remember { mutableStateOf(MutableInteractionSource()) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                role = Role.Checkbox,
                onClick = { onChecked(word) },
                indication = ripple(color = Color.Blue),
                interactionSource = interactionSource,
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${word.lexeme} (${word.translation})",
            fontSize = 16.sp
        )

        Checkbox(
            checked = word.isChecked,
            onCheckedChange = null // Set to null as the parent Row handles the click
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WordSelectionContentPreview() {
    WordSelectionContent(
        WordSelectionState(
            "Animals",
            categoryWords = listOf(
                WordUI(1, "Cat", "Кіт", true),
                WordUI(2, "Dog", "Собака", false),
                WordUI(3, "Bird", "Птах", true)
            )
        )
    )
}
