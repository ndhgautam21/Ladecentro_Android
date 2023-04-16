package com.ladecentro.ui.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ladecentro.R
import com.ladecentro.adapter.ProductPageAdapter
import com.ladecentro.databinding.ActivityProductBinding
import com.ladecentro.paging.ProductLoadingAdapter
import com.ladecentro.util.LoadingDialog
import com.ladecentro.view_model.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var productAdapter: ProductPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        setUpUI()
        setUpObserver()
    }

    private fun setUpUI() {
        loadingDialog = LoadingDialog(this)
        binding.productRv.layoutManager = GridLayoutManager(this, 2)
        binding.productRv.adapter = productAdapter
            .withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter(),
                footer = ProductLoadingAdapter()
            )

        productAdapter.addLoadStateListener {
            // add custom load state for
            // initial product loading
            if (it.source.refresh is LoadState.Loading) loadingDialog.startLoading() else loadingDialog.stopLoading()
        }
    }

    private fun setUpObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                productAdapter.submitData(it)
            }
        }
    }
}