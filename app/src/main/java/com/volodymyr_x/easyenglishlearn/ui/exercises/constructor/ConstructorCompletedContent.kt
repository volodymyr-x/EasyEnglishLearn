package com.volodymyr_x.easyenglishlearn.ui.exercises.constructor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.volodymyr_x.easyenglishlearn.R
import com.volodymyr_x.easyenglishlearn.ui.base_composables.VerticalSpacer

@Composable
fun ConstructorCompletedContent(
    state: ConstructorStageState,
    closeAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.constructor_completed),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )

            VerticalSpacer()

            Text(
                text = stringResource(R.string.quiz_error_score, state.errorCount),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            VerticalSpacer()

            Button(onClick = closeAction) {
                Text(text = stringResource(R.string.close))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConstructorCompletedContentPreview() {
    ConstructorCompletedContent(
        state = ConstructorStageState(
            errorCount = 3
        ),
        closeAction = {}
    )
}
