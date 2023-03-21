package com.ladecentro.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.textfield.TextInputLayout
import com.ladecentro.R
import com.ladecentro.databinding.ActivityProfileDetailsBinding
import com.ladecentro.listener.NetworkCallback
import com.ladecentro.model.ErrorResponse
import com.ladecentro.util.BottomSheetProfile
import com.ladecentro.util.LoadingDialog
import com.ladecentro.util.toast
import com.ladecentro.validation.InputValidation
import com.ladecentro.view_model.ProfileDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDetailsBinding
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel by viewModels<ProfileDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_details)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        loadingDialog = LoadingDialog(this)
        viewModel.getUserDetails(object : NetworkCallback {
            override fun onSuccess(message: String) {
            }

            override fun onError(error: ErrorResponse) {
                runOnUiThread {
                    toast(error.message)
                }
            }
        })

        binding.updateProfile.setOnClickListener {
            viewModel.updateProfile(object : NetworkCallback {
                override fun onSuccess(message: String) {
                    runOnUiThread {
                        toast(message)
                        setResult(RESULT_OK)
                        finish()
                    }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        toast(error.message)
                    }
                }
            })
        }
        observer()

        binding.fullName.editText?.addTextChangedListener(object :
            InputValidation(binding.fullName) {
            override fun validate(textInputLayout: TextInputLayout, text: String) {
                textInputLayout.error = if (validName(text)) null else "Invalid name"
                viewModel.errorEnable.postValue(!validName(text))
            }
        })
        val bottomSheet = BottomSheetProfile()
        binding.selectImage.setOnClickListener {
            bottomSheet.show(supportFragmentManager, "BottomSheetProfileDetails")
        }
    }

    private fun observer() {
        viewModel.loadingLD.observe(this) {
            if (it) loadingDialog.startLoading()
            else loadingDialog.stopLoading()
        }
        viewModel.userLd.observe(this) { user ->
            viewModel.setUserValues(user)
        }
    }

    fun deleteProfileImage() {
        viewModel.updateProfileImage(object : NetworkCallback {

            override fun onSuccess(message: String) {
                runOnUiThread {
                    toast(message)
                    setResult(RESULT_OK)
                    finish()
                }
            }

            override fun onError(error: ErrorResponse) {
                runOnUiThread {
                    toast(error.message)
                }
            }
        }, null)
    }

    fun openGalleryImages() {

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100
                )
            return
        }
        contract.launch("image/*")
    }

    /**
     * contract for select image
     * from external storage
     */
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        uri?.let {
            val fileDir = applicationContext.filesDir
            val file = File(fileDir, "images.png")
            val inputStream = contentResolver.openInputStream(it)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = Part.createFormData("file", file.name, requestBody)
            viewModel.updateProfileImage(object : NetworkCallback {

                override fun onSuccess(message: String) {
                    runOnUiThread {
                        toast(message)
                        setResult(RESULT_OK)
                        finish()
                    }
                }

                override fun onError(error: ErrorResponse) {
                    runOnUiThread {
                        toast(error.message)
                    }
                }
            }, part)
            binding.profileImage.setImageURI(it)
        }
    }

    /**
     * on request permissions result...
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            contract.launch("image/*")
        } else {
            toast("permission denied")
        }
    }
}