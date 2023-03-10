package com.ladecentro.ui.address

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ladecentro.R
import com.ladecentro.adapter.AddressAdapter
import com.ladecentro.databinding.ActivityAddressBinding
import com.ladecentro.listener.AddressListener
import com.ladecentro.model.response.AddressResponse
import com.ladecentro.model.response.ErrorResponse
import com.ladecentro.service.auth.AddressService
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.view_model.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressActivity : AppCompatActivity(), AddressListener, AddressService {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var viewModel: AddressViewModel
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)

        loadingDialog = LoadingDialog(this)
        viewModel = ViewModelProvider(this)[AddressViewModel::class.java]
        viewModel.addressService = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = AddressAdapter(applicationContext, this)
        binding.addressRecyclerView.adapter = adapter
        observer()
        onClickListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAddresses()
    }

    /**
     *
     */
    override fun deleteAddress(address: AddressResponse) {
        viewModel.deleteAddress(address.id!!)
    }

    override fun getAddress(address: AddressResponse) {
        val intent = Intent(applicationContext, AddAddressActivity::class.java)
        intent.putExtra(Constants.AddressId.name, address.id)
        startActivity(intent)
    }

    /**
     *
     */
    private fun observer() {
        viewModel.addressesLD.observe(this) { addresses ->
            val adapter = AddressAdapter(applicationContext, this)
            binding.addressRecyclerView.adapter = adapter
            adapter.submitList(addresses)
        }
        viewModel.loadingLD.observe(this) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }
    }

    private fun onClickListener() {
        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, AddAddressActivity::class.java)
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun success(message: String) {
        runOnUiThread {
            toast(message)
        }
    }

    override fun error(error: ErrorResponse) {
        runOnUiThread {
            toast(error.message)
        }
    }
}