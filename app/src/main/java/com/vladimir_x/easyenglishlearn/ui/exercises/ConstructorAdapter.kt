package com.vladimir_x.easyenglishlearn.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvLetterItemBinding

object LetterDiff : DiffUtil.ItemCallback<Letter>() {
    override fun areItemsTheSame(oldItem: Letter, newItem: Letter) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Letter, newItem: Letter) =
        oldItem.value == newItem.value
}

class ConstructorAdapter(
    private val listener: (letter: Letter) -> Unit
) : ListAdapter<Letter, ConstructorAdapter.LetterViewHolder>(LetterDiff) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LetterViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = RvLetterItemBinding.inflate(
            inflater,
            parent,
            false
        )
        return LetterViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class LetterViewHolder(
        private val binding: RvLetterItemBinding,
        private val listener: (letter: Letter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: Letter) {
            with(binding) {
                buttonLetter.text = letter.value
                buttonLetter.setOnClickListener {
                    listener.invoke(letter)
                }
            }
        }
    }

}