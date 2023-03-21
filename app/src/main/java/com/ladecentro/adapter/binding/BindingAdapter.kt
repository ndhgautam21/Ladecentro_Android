package com.ladecentro.adapter.binding

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ladecentro.R

@BindingAdapter(value = ["android:loadImage"])
fun ImageView.imageFromBinary(profileImage: String?) {

    if (profileImage == null) {
        this.setImageResource(R.drawable.user_profile)
        return
    }
    val byteArray = Base64.decode(profileImage, Base64.DEFAULT)
    val image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    this.setImageBitmap(image)
}

@BindingAdapter(value = ["android:loadImageUrl"])
fun ImageView.imageFromURL(url: String) {
    Glide.with(this.context).load(url).into(this)
}