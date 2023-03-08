package com.ladecentro.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ladecentro.R
import com.ladecentro.authentication.AuthActivity
import com.ladecentro.authentication.LoginFragment
import com.ladecentro.ui.home.HomeActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = myPreference.getStoredTag(Constants.PreferenceToken.name)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent : Intent = if (token.isNullOrBlank()) {
                Intent(applicationContext, AuthActivity::class.java)
            } else {
                Intent(applicationContext, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1000)
    }
}