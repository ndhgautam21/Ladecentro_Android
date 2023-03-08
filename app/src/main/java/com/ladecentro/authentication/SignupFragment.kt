package com.ladecentro.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ladecentro.R
import com.ladecentro.databinding.FragmentSignupBinding
import com.ladecentro.model.response.ErrorResponse
import com.ladecentro.service.auth.AuthService
import com.ladecentro.ui.home.HomeActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.MyPreference
import com.ladecentro.util.toast
import com.ladecentro.view_model.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment : Fragment(), AuthService {

    private var mBinding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = mBinding!!
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var loadingDialog: LoadingDialog

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_signup, container, false)
        signupViewModel = ViewModelProvider(requireActivity())[SignupViewModel::class.java]

        signupViewModel.authService = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = signupViewModel
        loadingDialog = LoadingDialog(requireActivity())

        signupViewModel.loadingLD.observe(viewLifecycleOwner) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginPage.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun success(token: String) {
        myPreference.setStoredTag(Constants.PreferenceToken.name, token)
        activity?.let { it ->
            it.runOnUiThread {
                requireActivity().toast("Account created")
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                it.finish()
            }
        }
    }

    override fun error(error: ErrorResponse) {
        activity?.runOnUiThread {
            requireActivity().toast(error.message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}