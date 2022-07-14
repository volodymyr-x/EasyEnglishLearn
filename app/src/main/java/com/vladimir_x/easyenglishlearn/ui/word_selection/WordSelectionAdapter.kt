package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvWordSelectionItemBinding
import com.vladimir_x.easyenglishlearn.ui.WordUIDiff
import com.vladimir_x.easyenglishlearn.ui.model.WordUI
import com.vladimir_x.easyenglishlearn.ui.word_selection.WordSelectionAdapter.WordSelectionHolder

class WordSelectionAdapter(
    private val listener: (checked: Boolean, wordId: Long) -> Unit
) : ListAdapter<WordUI, WordSelectionHolder>(WordUIDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordSelectionHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = RvWordSelectionItemBinding.inflate(
            inflater,
            parent,
            false
        )
        return WordSelectionHolder(binding, listener)
    }

    override fun onBindViewHolder(wordSelectionHolder: WordSelectionHolder, position: Int) {
        wordSelectionHolder.bind(currentList[position])
    }

    class WordSelectionHolder(
        private val binding: RvWordSelectionItemBinding,
        private val listener: (checked: Boolean, wordId: Long) -> Unit
    ) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(word: WordUI) {
            with(binding) {
                tvLexeme.text = word.lexeme
                tvTranslation.text = word.translation
                cbWordChoice.isChecked = word.isChecked
                cbWordChoice.setOnClickListener {
                    listener.invoke(cbWordChoice.isChecked, word.id)
                }
            }
        }
    }
}