package com.ladecentro.adapter.binding

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ladecentro.model.response.User.ProfileImage

@BindingAdapter(value = ["android:loadImage"])
fun ImageView.imageFromBinary(profileImage: String?) {

    profileImage?.let {
        val byteArray = Base64.decode(it, Base64.DEFAULT)
        val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        this.setImageBitmap(image)
    }
}