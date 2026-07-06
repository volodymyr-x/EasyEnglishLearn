package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
fun ConstructorStageContent(
    state: ConstructorStageState,
    event: (ConstructorEvent) -> Unit,
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

        Text(
            text = state.currentAnswer,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        VerticalSpacer()

        FlowRow(
            maxItemsInEachRow = 6,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.letters.forEach { text ->
                Button(
                    onClick = { event(ConstructorEvent.LetterButtonClicked(letter = text)) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text)
                }
            }
        }

        VerticalSpacer()

        Button(
            onClick = { event(ConstructorEvent.UndoButtonClicked) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Undo")
        }

        VerticalSpacer()

        if (state.incorrectAnswer.isNotEmpty()) {
            Text(
                text = stringResource(R.string.wrong_answer_with_value, state.incorrectAnswer),
                fontSize = 16.sp,
                color = Color.Red
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ConstructorStageContentPreview() {
    ConstructorStageContent(
        state = ConstructorStageState(
            question = "constructor",
            currentAnswer = "конструктор",
            letters = listOf("к", "о", "н", "с", "т", "р", "у", "к", "т", "о", "р"),
        ),
        event = { },
    )
}
