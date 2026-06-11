package com.volodymyr_x.easyenglishlearn.ui.exercises.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun QuizStageContent(
    state: QuizStageState,
    answerAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
