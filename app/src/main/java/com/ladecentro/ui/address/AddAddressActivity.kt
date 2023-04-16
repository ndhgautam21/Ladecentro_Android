package com.ladecentro.ui.address

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ladecentro.R
import com.ladecentro.databinding.ActivityAddAddressBinding
import com.ladecentro.listener.UIState
import com.ladecentro.model.ErrorResponse
import com.ladecentro.service.auth.AddressService
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.view_model.AddAddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<AddAddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_address)
        var addressId = intent.getStringExtra(Constants.AddressId.name)
        if (addressId == null) {
            addressId = ""
        }
        viewModel.addressId = addressId
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        loadingDialog = LoadingDialog(this)

        if (addressId.isNotBlank()) {
            lifecycleScope.launch {
                viewModel.getAddress(addressId).collect {
                    when (it) {
                        is UIState.Loading -> {
                            loadingDialog.startLoading()
                        }
                        is UIState.Success -> {
                            viewModel.setAddressValue(it.data)
                            loadingDialog.stopLoading()
                        }
                        is UIState.Error -> {
                            toast(it.errorResponse)
                            loadingDialog.stopLoading()
                        }
                    }
                }
            }
        }
        onClickListener()
    }

    private fun onClickListener() {
        binding.back.setOnClickListener {
            finish()
        }
        binding.saveAddress.setOnClickListener {
            lifecycleScope.launch {
                viewModel.createAddress.collect {
                    when (it) {
                        is UIState.Loading -> {
                            loadingDialog.startLoading()
                        }
                        is UIState.Success -> {
                            toast("created successfully!!")
                            loadingDialog.stopLoading()
                            setResult(RESULT_OK)
                            finish()
                        }
                        is UIState.Error -> {
                            toast(it.errorResponse)
                            loadingDialog.stopLoading()
                        }
                    }
                }
            }
        }
    }
}