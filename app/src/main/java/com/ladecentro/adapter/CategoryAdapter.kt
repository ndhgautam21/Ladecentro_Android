package com.ladecentro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ladecentro.databinding.SampleCategoryBinding
import com.ladecentro.model.Category
import javax.inject.Inject

class CategoryAdapter @Inject constructor() :
    ListAdapter<Category, CategoryAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(val binding: SampleCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SampleCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        if (category != null) {
            holder.binding.category = category
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}