package com.ladecentro.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ladecentro.R
import com.ladecentro.databinding.BottomSheetProfileBinding
import com.ladecentro.ui.profile.ProfileDetailsActivity

class BottomSheetProfile : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_profile, container, false)
        binding.gallery.setOnClickListener {
            (activity as ProfileDetailsActivity).openGalleryImages()
        }
        binding.deleteProfile.setOnClickListener {
            (activity as ProfileDetailsActivity).deleteProfileImage()
        }
        return binding.root
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogTheme
}