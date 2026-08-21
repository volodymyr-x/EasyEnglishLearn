package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.ui.base_composables.LoadingScreen
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer
import com.volodymyr_x.easyenglishlearn.ui.word_selection.WordSelectionResult

@Composable
fun ExerciseQuizScreen(
    wordSelectionResult: WordSelectionResult,
    closeFragmentAction: () -> Unit
) {
    val viewModel = hiltViewModel { factory: QuizViewModel.Factory ->
        factory.create(wordSelectionResult)
    }
    when (val screenState = viewModel.exerciseState.collectAsStateWithLifecycle().value) {
        is QuizState.LoadingState -> LoadingScreen()
        is QuizState.CompletedState -> QuizCompletedContent(
            state = screenState.data,
            closeAction = closeFragmentAction
        )
        is QuizState.StageState -> QuizStageContent(
            state = screenState.data,
            answerAction = viewModel::onAnswerChecked
        )
    }
}

@Composable
fun QuizStageContent(
    state: QuizStageState,
    answerAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSourceState = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.question,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        VerticalSpacer()

        Column(
            modifier = modifier.selectableGroup(),
            horizontalAlignment = Alignment.Start,
        ) {
            state.answers.forEach { text ->
                Row(
                    Modifier
                        .height(56.dp)
                        .selectable(
                            selected = false,
                            onClick = { answerAction(text) },
                            indication = ripple(color = Color.Blue),
                            interactionSource = interactionSourceState,
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == state.incorrectAnswer),
                        onClick = null // Row handles the click
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        VerticalSpacer()

        if (state.incorrectAnswer.isNotEmpty()) {
            Text(
                text = stringResource(R.string.wrong_answer),
                fontSize = 16.sp,
                color = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizStageContentPreview() {
    QuizStageContent(
        state = QuizStageState(
            question = "What is the translation of 'cat'?",
            answers = listOf("кіт", "собака", "птиця")
        ),
        answerAction = {}
    )
}
