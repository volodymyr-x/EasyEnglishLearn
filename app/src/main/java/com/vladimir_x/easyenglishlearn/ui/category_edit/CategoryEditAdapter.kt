package com.vladimir_x.easyenglishlearn.ui.category_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvCategoryEditItemBinding
import com.vladimir_x.easyenglishlearn.ui.WordUIDiff
import com.vladimir_x.easyenglishlearn.ui.model.WordUI

class CategoryEditAdapter(
    private val clickListener: (word: WordUI) -> Unit,
    private val removeClickListener: (word: WordUI) -> Unit
) : ListAdapter<WordUI, CategoryEditHolder>(WordUIDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEditHolder {
        return CategoryEditHolder.from(parent, clickListener, removeClickListener)
    }

    override fun onBindViewHolder(categoryEditHolder: CategoryEditHolder, position: Int) {
        categoryEditHolder.bind(currentList[position])
    }
}

class CategoryEditHolder(
    private val itemBinding: RvCategoryEditItemBinding,
    private val clickListener: (word: WordUI) -> (Unit),
    private val removeClickListener: (word: WordUI) -> (Unit)
) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(word: WordUI) {
        with(itemBinding) {
            tvLexeme.text = word.lexeme
            tvTranslation.text = word.translation
            ivWordRemove.setOnClickListener { removeClickListener.invoke(word) }
            root.setOnClickListener { clickListener.invoke(word) }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            clickListener: (word: WordUI) -> (Unit),
            removeClickListener: (word: WordUI) -> (Unit)
        ): CategoryEditHolder {
            val binding = RvCategoryEditItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryEditHolder(binding, clickListener, removeClickListener)
        }
    }

}