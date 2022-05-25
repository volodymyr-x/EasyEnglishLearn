package com.vladimir_x.easyenglishlearn.ui.word_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvWordSelectionItemBinding
import com.vladimir_x.easyenglishlearn.model.Word
import com.vladimir_x.easyenglishlearn.ui.word_selection.WordSelectionAdapter.WordSelectionHolder
import java.util.ArrayList

class WordSelectionAdapter(
    private val listener: (checked: Boolean, word: Word) -> Unit
) : RecyclerView.Adapter<WordSelectionHolder>() {
    private var wordList: List<Word> = ArrayList<Word>()

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
        wordSelectionHolder.bind(wordList[position])
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    fun setWordList(wordList: List<Word>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }

    class WordSelectionHolder(
        private val binding: RvWordSelectionItemBinding,
        private val listener: (checked: Boolean, word: Word) -> Unit
    ) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(word: Word) {
            with(binding) {
                tvLexeme.text = word.lexeme
                tvTranslation.text = word.translation
                cbWordChoice.isChecked = word.isChecked
                cbWordChoice.setOnCheckedChangeListener { _, isChecked ->
                    listener.invoke(isChecked, word)
                }
            }
        }
    }
}