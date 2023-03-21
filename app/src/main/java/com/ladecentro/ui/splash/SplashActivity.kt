package com.ladecentro.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ladecentro.R
import com.ladecentro.authentication.AuthActivity
import com.ladecentro.ui.home.HomeActivity
import com.ladecentro.util.Constants
import com.ladecentro.util.MyPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var myPreference: MyPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = myPreference.getStoredTag(Constants.PreferenceToken.name)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent : Intent = if (token.isBlank()) {
                Intent(applicationContext, AuthActivity::class.java)
            } else {
                Intent(applicationContext, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 500)
    }
}