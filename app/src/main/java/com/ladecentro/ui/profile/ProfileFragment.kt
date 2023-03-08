package com.ladecentro.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ladecentro.R
import com.ladecentro.authentication.AuthActivity
import com.ladecentro.databinding.FragmentProfileBinding
import com.ladecentro.model.response.ErrorResponse
import com.ladecentro.service.auth.AuthService
import com.ladecentro.ui.address.AddressActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.MyPreference
import com.ladecentro.util.toast
import com.ladecentro.view_model.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), AuthService {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        viewModel.authService = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        loadingDialog = LoadingDialog(requireActivity())
        observer()
        onClickListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUserDetails()
    }

    /**
     *
     */
    private fun observer() {
        viewModel.loadingLD.observe(viewLifecycleOwner) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }
    }

    /**
     *
     */
    private fun onClickListener() {

        binding.accountDetails.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileDetailsActivity::class.java))
        }
        binding.addresses.setOnClickListener {
            startActivity(Intent(requireContext(), AddressActivity::class.java))
        }

        binding.button.setOnClickListener {
            myPreference.removeStoresTag(Constants.PreferenceToken.name)
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            activity?.finish()
        }
    }

    override fun success(token: String) {
        activity?.runOnUiThread {
            requireActivity().toast(token)
        }
    }

    override fun error(error: ErrorResponse) {
        activity?.runOnUiThread {
            requireActivity().toast(error.message)
        }
    }
}