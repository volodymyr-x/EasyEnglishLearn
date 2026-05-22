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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun QuizContent(
    state: QuizStageState,
    answerAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (selectedOption, _) = remember { mutableStateOf("") }

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
                            selected = false /*(text == selectedOption)*/,
                            onClick = { answerAction(text) },
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
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
    }
}

@Preview(showBackground = true)
@Composable
fun QuizContentPreview() {
    QuizContent(
        state = QuizStageState(
            question = "What is the translation of 'cat'?",
            answers = listOf("кіт", "собака", "птиця")
        ),
        answerAction = {}
    )
}
