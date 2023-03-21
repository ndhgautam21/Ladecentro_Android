package com.ladecentro.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ladecentro.R
import com.ladecentro.authentication.AuthActivity
import com.ladecentro.databinding.FragmentProfileBinding
import com.ladecentro.listener.NetworkCallback
import com.ladecentro.model.ErrorResponse
import com.ladecentro.ui.address.AddressActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.MyPreference
import com.ladecentro.util.toast
import com.ladecentro.view_model.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        loadingDialog = LoadingDialog(requireActivity())
        callGetUserDetails()
        observer()
        onClickListener()
        return binding.root
    }

    private fun callGetUserDetails() {

        viewModel.getUserDetails(object : NetworkCallback {
            override fun onSuccess(message: String) {
                activity?.runOnUiThread {
                    requireActivity().toast(message)
                }
            }

            override fun onError(error: ErrorResponse) {
                activity?.runOnUiThread {
                    requireActivity().toast(error.message)
                }
            }
        })
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
            activityResults.launch(Intent(requireContext(), ProfileDetailsActivity::class.java))
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

    /**
     * after finishing profile details activity
     * launch this activity contract
     */
    private val activityResults = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            callGetUserDetails()
        }
    }
}