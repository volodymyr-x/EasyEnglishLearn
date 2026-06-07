package com.volodymyr_x.easyenglishlearn.ui.exercises

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun ConstructorContent(
    state: DataDto.ConstructorDto,
    letterButtonAction: (Char) -> Unit,
    undoButtonAction: () -> Unit,
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
            text = state.answer,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        VerticalSpacer()

        /*FlowRow(
            maxItemsInEachRow = 6,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.letters.forEach { text ->
                Button(
                    onClick = { letterButtonAction(text) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text.toString())
                }
            }
        }*/

        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.letters) { text ->
                Button(
                    onClick = { letterButtonAction(text) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text.toString())
                }
            }

        }

        VerticalSpacer()

        Button(
            onClick = { undoButtonAction() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Undo")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ConstructorContentPreview() {
    ConstructorContent(
        state = DataDto.ConstructorDto(
            question = "constructor",
            answer = "конструктор",
            letters = listOf('к', 'о', 'н', 'с', 'т', 'р', 'у', 'к', 'т', 'о', 'р')
        ),
        letterButtonAction = {},
        undoButtonAction = {}
    )
}
