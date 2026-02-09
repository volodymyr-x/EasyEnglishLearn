package com.volodymyr_x.easyenglishlearn.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            )
        ) {
            androidx.compose.material3.Text(text)
        }
}

@Preview()
@Composable
fun BaseButtonPreview() {
    BaseButton(text = "Click Me", onClick = {})
}



