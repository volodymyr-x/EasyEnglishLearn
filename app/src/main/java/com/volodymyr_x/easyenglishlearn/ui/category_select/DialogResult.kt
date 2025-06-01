package com.volodymyr_x.easyenglishlearn.ui.category_select

import java.io.Serializable

sealed class DialogResult : Serializable {
    data object Yes: DialogResult() {
        private fun readResolve(): Any = Yes
    }

    data object No: DialogResult() {
        private fun readResolve(): Any = No
    }
}
