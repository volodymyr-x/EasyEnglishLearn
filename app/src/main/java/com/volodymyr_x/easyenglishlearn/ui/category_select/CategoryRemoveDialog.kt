package com.volodymyr_x.easyenglishlearn.ui.category_select

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.volodymyr_x.easyenglishlearn.R

@Composable
fun CategoryRemoveDialog(
    categoryName: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
        ) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = stringResource(R.string.rcf_tv_remove_category)) },
            text = { Text(text = categoryName) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(text = stringResource(R.string.rcf_btn_yes))
                }
            },
            dismissButton = {
                Button(onClick = onDismissRequest) {
                    Text(text = stringResource(R.string.rcf_btn_no))
                }
            },
            modifier = modifier
        )
    }

@Preview
@Composable
fun CategoryRemoveDialogPreview() {
    CategoryRemoveDialog(
        categoryName = "Sample Category",
        onDismissRequest = {},
        onConfirm = {}
    )
}
