package com.ladecentro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ladecentro.adapter.ProductPageAdapter
import com.ladecentro.databinding.ActivityTestBinding
import com.ladecentro.view_model.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private val viewModel by viewModels<ShopViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        val adapter = ProductPageAdapter()
        binding.productRvs.adapter = adapter
        binding.productRvs.layoutManager = LinearLayoutManager(this)

//        viewModel.uiState.collectLatest {
//            adapter.submitData(lifecycle, it)
//        }
    }
}