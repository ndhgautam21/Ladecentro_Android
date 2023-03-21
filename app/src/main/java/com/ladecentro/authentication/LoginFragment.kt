package com.ladecentro.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.ladecentro.R
import com.ladecentro.databinding.FragmentLoginBinding
import com.ladecentro.model.ErrorResponse
import com.ladecentro.service.auth.AuthService
import com.ladecentro.ui.home.HomeActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.MyPreference
import com.ladecentro.util.toast
import com.ladecentro.validation.InputValidation
import com.ladecentro.view_model.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), AuthService {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)

        viewModel.authService = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        loadingDialog = LoadingDialog(requireActivity())

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }

        binding.email.editText?.addTextChangedListener(object : InputValidation(binding.email) {

            override fun validate(textInputLayout: TextInputLayout, text: String) {
                viewModel.errorEmail.postValue(!validEmail(text))
                textInputLayout.error = if (validEmail(text)) null else "Invalid email"
            }
        })

        binding.password.editText?.addTextChangedListener(object :
            InputValidation(binding.password) {

            override fun validate(textInputLayout: TextInputLayout, text: String) {
                viewModel.errorPassword.postValue(!validName(text))
                textInputLayout.error = if (validName(text)) null else "Invalid email"
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInPage.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun error(error: ErrorResponse) {
        activity?.runOnUiThread {
            requireActivity().toast(error.message)
        }
    }

    override fun success(token: String) {
        myPreference.setStoredTag(Constants.PreferenceToken.name, token)
        activity?.let { it ->
            it.runOnUiThread {
                requireActivity().toast("Logged in successfully")
                startActivity(Intent(requireContext(), HomeActivity::class.java))
                it.finish()
            }
        }
    }
}