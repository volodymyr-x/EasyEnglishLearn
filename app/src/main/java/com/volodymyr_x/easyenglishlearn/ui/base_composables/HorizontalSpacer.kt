package com.volodymyr_x.easyenglishlearn.ui.base_composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(
    modifier: Modifier = Modifier,
    width: Int = 16
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .width(width.dp)
    )
}
