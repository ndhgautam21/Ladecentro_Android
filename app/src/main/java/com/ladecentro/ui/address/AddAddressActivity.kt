package com.ladecentro.ui.address

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ladecentro.R
import com.ladecentro.databinding.ActivityAddAddressBinding
import com.ladecentro.model.response.ErrorResponse
import com.ladecentro.service.auth.AddressService
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.view_model.AddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAddressActivity : AppCompatActivity(), AddressService {

    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var viewModel: AddAddressViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        var addressId = intent.getStringExtra(Constants.AddressId.name)
        if (addressId == null) {
            addressId = ""
        }
        viewModel = ViewModelProvider(this)[AddAddressViewModel::class.java]
        viewModel.addressService = this
        viewModel.addressId = addressId
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        loadingDialog = LoadingDialog(this)
        observer()
        onClickListener()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAddress()
    }

    private fun observer() {
        viewModel.loadingLD.observe(this) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }

        viewModel.addressLD.observe(this) {
            viewModel.setAddressValue(it)
        }
    }

    private fun onClickListener() {
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun success(message: String) {
        runOnUiThread {
            toast(message)
            finish()
        }
    }

    override fun error(error: ErrorResponse) {
        runOnUiThread {
            toast(error.message)
        }
    }
}