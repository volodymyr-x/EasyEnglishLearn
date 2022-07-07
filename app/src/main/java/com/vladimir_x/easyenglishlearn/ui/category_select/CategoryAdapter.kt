package com.vladimir_x.easyenglishlearn.ui.category_select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vladimir_x.easyenglishlearn.databinding.RvCategoryItemBinding

private val diffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}

class CategoryAdapter(
    private val itemClickListener: (categoryName: String) -> (Unit),
    private val editClickListener: (categoryName: String) -> (Unit),
    private val removeClickListener: (categoryName: String) -> (Unit)
) : ListAdapter<String, CategoryViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(
            parent,
            itemClickListener,
            editClickListener,
            removeClickListener
        )
    }

    override fun onBindViewHolder(categoryViewHolder: CategoryViewHolder, position: Int) {
        categoryViewHolder.bind(currentList[position])
    }
}

class CategoryViewHolder(
    private val binding: RvCategoryItemBinding,
    private val itemClickListener: (categoryName: String) -> (Unit),
    private val editClickListener: (categoryName: String) -> (Unit),
    private val removeClickListener: (categoryName: String) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryName: String) {
        binding.categoryName.text = categoryName
        itemView.setOnClickListener {
            itemClickListener.invoke(categoryName)
        }
        binding.categoryEdit.setOnClickListener {
            editClickListener.invoke(categoryName)
        }
        binding.categoryRemove.setOnClickListener {
            removeClickListener.invoke(categoryName)
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemClickListener: (categoryName: String) -> (Unit),
            editClickListener: (categoryName: String) -> (Unit),
            removeClickListener: (categoryName: String) -> (Unit)
        ): CategoryViewHolder {
            val binding = RvCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CategoryViewHolder(
                binding,
                itemClickListener,
                editClickListener,
                removeClickListener
            )
        }
    }
}