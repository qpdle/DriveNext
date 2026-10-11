package ru.mtuci.drivenext

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

@SuppressLint("CustomSplashScreen")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_DriveNext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            checkInternetAndProceed()
        }, 2000)
    }

    private fun checkInternetAndProceed() {
        if (!isNetworkAvailable()) {
            val intent = Intent(this, NoInternetActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            checkLaunchState()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun checkLaunchState() {
        val sharedPref = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val isOnboardingCompleted = sharedPref.getBoolean("is_onboarding_completed", false)

        if (!isOnboardingCompleted) {
            // Если первый запуск — открываем онбординг
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        } else {
            // Если онбординг уже пройден — сразу открываем выбор действия (Вход/Регистрация)
            val intent = Intent(this, AuthChoiceActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}