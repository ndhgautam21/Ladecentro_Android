package com.ladecentro.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.ladecentro.databinding.PagingLoaderBinding
import androidx.recyclerview.widget.RecyclerView
import com.ladecentro.util.LoadingDialog

class ProductLoadingAdapter : LoadStateAdapter<ProductLoadingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PagingLoaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.binding.spinKit.visibility =
            if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = PagingLoaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
}