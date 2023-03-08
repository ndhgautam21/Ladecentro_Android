package com.ladecentro.validation

import android.text.Editable
import android.text.TextWatcher
import androidx.core.text.isDigitsOnly
import com.google.android.material.textfield.TextInputLayout

abstract class InputValidation(private val textInputLayout: TextInputLayout) : TextWatcher {

    abstract fun validate(textInputLayout: TextInputLayout, text: String)

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        val text = textInputLayout.editText?.text.toString().trim()
        validate(textInputLayout, text)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        val text = textInputLayout.editText?.text.toString().trim()
        validate(textInputLayout, text)
    }

    fun validName(string: String): Boolean {
        return if (string.isBlank()) false
        else string.length <= 25
    }

    fun validEmail(string: String): Boolean {
        return !(string.isEmpty() || string.length > 50 || string.isDigitsOnly())
    }

    fun validPhoneNo(string: String): Boolean {
        return !(string.isEmpty() || string.length != 13)
    }

    fun validId(string: String): Boolean {
        return !(string.isEmpty() || !string.isDigitsOnly() || string.length != 6)
    }
}