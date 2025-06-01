package com.volodymyr_x.easyenglishlearn.ui.category_select

import java.io.Serializable

sealed class DialogResult : Serializable {
    object Yes: DialogResult()
    object No: DialogResult()
}
