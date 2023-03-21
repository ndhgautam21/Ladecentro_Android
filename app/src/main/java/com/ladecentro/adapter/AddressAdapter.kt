package com.ladecentro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ladecentro.R
import com.ladecentro.databinding.SampleAddressBinding
import com.ladecentro.listener.AddressListener
import com.ladecentro.model.AddressResponse

class AddressAdapter(
    private val context: Context,
    private val listener: AddressListener
) : ListAdapter<AddressResponse, AddressAdapter.ViewHolder>(DifferCallback()) {

    inner class ViewHolder(val sampleAddressBinding: SampleAddressBinding) :
        RecyclerView.ViewHolder(sampleAddressBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SampleAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.sampleAddressBinding.address = getItem(position)
        holder.sampleAddressBinding.executePendingBindings()
        holder.sampleAddressBinding.menuIcon.setOnClickListener {
            popupMenu(it, position)
        }
    }

    /**
     * DiffUtil itemCallback
     */
    class DifferCallback : DiffUtil.ItemCallback<AddressResponse>() {

        override fun areItemsTheSame(
            oldItem: AddressResponse,
            newItem: AddressResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AddressResponse,
            newItem: AddressResponse
        ): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * show popup menu for the edit and delete address
     *
     * @param view view
     * @param position position of item
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun popupMenu(view: View, position: Int) {
        val addressResponse = getItem(position)
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_address)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.edit_address -> listener.getAddress(addressResponse)
                R.id.delete_address -> {
                    listener.deleteAddress(addressResponse)
                }
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }
}