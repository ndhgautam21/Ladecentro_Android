package com.ladecentro.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.ladecentro.R
import com.ladecentro.adapter.CategoryAdapter
import com.ladecentro.adapter.ProductPageAdapter
import com.ladecentro.databinding.FragmentShopBinding
import com.ladecentro.listener.UIState
import com.ladecentro.paging.ProductLoadingAdapter
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.view_model.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var adapter: ProductPageAdapter

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        binding.urls = "https://ladecentro.in/wp-content/uploads/2022/10/Creative-Banner.png"
        binding.lifecycleOwner = viewLifecycleOwner
        setUpUI()
        setUpObserver()
        return binding.root
    }

    private fun setUpUI() {
        loadingDialog = LoadingDialog(requireActivity())
        binding.productRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.categoryRv.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.categoryRv.adapter = categoryAdapter
        // setting adapter with the loading in header and footer
        binding.productRv.adapter = adapter
            .withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter(),
                footer = ProductLoadingAdapter()
            )

        adapter.addLoadStateListener {
            // add custom load state for
            // initial product loading
            if (it.source.refresh is LoadState.Loading) loadingDialog.startLoading() else loadingDialog.stopLoading()
        }
    }

    private fun setUpObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            viewModel.categoryUiState.collect {
                when(it) {
                    is UIState.Loading -> {
                        loadingDialog.startLoading()
                    }
                    is UIState.Success -> {
                        categoryAdapter.submitList(it.data)
                        loadingDialog.stopLoading()
                    }
                    is UIState.Error -> {
                        activity?.toast(it.errorResponse)
                    }
                }
            }
        }

        binding.bannerProduct.setOnClickListener {
            startActivity(Intent(requireActivity(), ProductActivity::class.java))
        }
    }
}