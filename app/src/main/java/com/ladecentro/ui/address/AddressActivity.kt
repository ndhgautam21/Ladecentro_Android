package com.ladecentro.ui.address

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ladecentro.R
import com.ladecentro.adapter.AddressAdapter
import com.ladecentro.databinding.ActivityAddressBinding
import com.ladecentro.listener.AddressListener
import com.ladecentro.listener.UIState
import com.ladecentro.model.AddressResponse
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.view_model.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressActivity : AppCompatActivity(), AddressListener {

    private lateinit var binding: ActivityAddressBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<AddressViewModel>()
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)
        setUpUI()
        setUpObserver()
        setUpOnClickListener()
    }

    private fun setUpUI() {
        loadingDialog = LoadingDialog(this)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = AddressAdapter(applicationContext, this)
        binding.addressRecyclerView.adapter = adapter
    }

    /**
     *
     */
    override fun deleteAddress(address: AddressResponse) {
        lifecycleScope.launch {
            viewModel.deleteAddress(address.id!!).collect {
                when (it) {
                    is UIState.Loading -> {
                        loadingDialog.startLoading()
                    }
                    is UIState.Success -> {
                        loadingDialog.stopLoading()
                        toast("Deleted successfully !!")
                        viewModel.getAddresses()
                    }
                    is UIState.Error -> {
                        loadingDialog.stopLoading()
                    }
                }
            }
        }
    }

    override fun getAddress(address: AddressResponse) {
        val intent = Intent(applicationContext, AddAddressActivity::class.java)
        intent.putExtra(Constants.AddressId.name, address.id)
        activityResults.launch(intent)
    }

    /**
     *
     */
    private fun setUpObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addresses.collect {
                    when (it) {
                        is UIState.Loading -> {
                            loadingDialog.startLoading()
                        }
                        is UIState.Success -> {
                            adapter.submitList(it.data)
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
    }

    private fun setUpOnClickListener() {
        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, AddAddressActivity::class.java)
            activityResults.launch(intent)
        }
        binding.back.setOnClickListener {
            finish()
        }
    }

    private val activityResults = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getAddresses()
        }
    }
}