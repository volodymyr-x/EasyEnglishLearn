package com.vladimir_x.easyenglishlearn.category_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvCategoryEditItemBinding
import com.vladimir_x.easyenglishlearn.model.Word
import java.util.ArrayList

class CategoryEditAdapter(private val clickListener: (word: Word) -> Unit) :
    RecyclerView.Adapter<CategoryEditHolder>() {
    private var wordList: List<Word> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryEditHolder {
        return CategoryEditHolder.from(parent, clickListener)
    }

    override fun onBindViewHolder(categoryEditHolder: CategoryEditHolder, position: Int) {
        categoryEditHolder.bind(wordList[position])
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    fun setWordList(wordList: List<Word>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }
}

class CategoryEditHolder(
    private val itemBinding: RvCategoryEditItemBinding,
    private val clickListener: (word: Word) -> (Unit)
) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(word: Word) {
        with(itemBinding) {
            ceiTvLexeme.text = word.lexeme
            ceiTvTranslation.text = word.translation
            ceiIvWordRemove.setOnClickListener { clickListener.invoke(word) }
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            clickListener: (word: Word) -> (Unit)
        ): CategoryEditHolder {
            val binding = RvCategoryEditItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryEditHolder(binding, clickListener)
        }
    }

}