package com.vladimir_x.easyenglishlearn.ui

import androidx.recyclerview.widget.DiffUtil
import com.vladimir_x.easyenglishlearn.ui.model.WordUI

object WordUIDiff : DiffUtil.ItemCallback<WordUI>() {
    override fun areItemsTheSame(oldItem: WordUI, newItem: WordUI) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: WordUI, newItem: WordUI) = oldItem == newItem
}