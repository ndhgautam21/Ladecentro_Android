package com.ladecentro.ui.shop

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
import com.ladecentro.adapter.ProductPageAdapter
import com.ladecentro.databinding.FragmentShopBinding
import com.ladecentro.paging.ProductLoadingAdapter
import com.ladecentro.util.LoadingDialog
import com.ladecentro.view_model.ShopViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

        binding.productRv.adapter = adapter
            .withLoadStateHeaderAndFooter(
                header = ProductLoadingAdapter(),
                footer = ProductLoadingAdapter()
            )

        adapter.addLoadStateListener {
            if (it.source.refresh is LoadState.Loading) loadingDialog.startLoading() else loadingDialog.stopLoading()
        }
    }

    private fun setUpObserver() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}