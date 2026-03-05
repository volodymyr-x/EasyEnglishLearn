package com.volodymyr_x.easyenglishlearn.ui.word_selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.volodymyr_x.easyenglishlearn.Constants
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun ExerciseChoiceContent(
    onDismissRequest: () -> Unit,
    onConfirmation: (ExerciseChoiceDto) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf("word -> translation", "translation -> word")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.ecf_tv_title),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                VerticalSpacer()

                Text(
                    text = stringResource(R.string.ecf_tv_translation_direction),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                VerticalSpacer()

                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .height(56.dp)
                            // Use selectable to handle the selection and interaction
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null // null recommended when Row handles the click
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }

                VerticalSpacer()

                Button(
                    onClick = {
                        onConfirmation(
                            ExerciseChoiceDto(
                                isWordToTranslation = selectedOption == radioOptions[0],
                                exerciseType = Constants.WORD_QUIZ
                            )
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.ecf_btn_quiz))
                }

                VerticalSpacer()

                Button(
                    onClick = {
                        onConfirmation(
                            ExerciseChoiceDto(
                                isWordToTranslation = selectedOption == radioOptions[0],
                                exerciseType = Constants.WORD_CONSTRUCTOR
                            )
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.ecf_btn_constructor))
                }

                VerticalSpacer()

                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(R.string.ecf_btn_cancel))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ExerciseChoiceContentPreview() {
    ExerciseChoiceContent(
        onDismissRequest = {},
        onConfirmation = {}
    )
}
