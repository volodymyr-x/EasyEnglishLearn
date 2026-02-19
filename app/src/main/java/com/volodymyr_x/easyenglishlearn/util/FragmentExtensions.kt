package com.volodymyr_x.easyenglishlearn.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

fun Fragment.setComposeContent(composeView: ComposeView, content: @Composable () -> Unit) {
    composeView.setViewCompositionStrategy(
        ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
    )
    composeView.setContent {
        content()
    }
}
