package com.dxshulya.wasabi.ui.splash

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.data.datastore.SharedPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private fun showErrorWindow() {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(getString(R.string.error_window_title))
                .setMessage("Проверьте соединение с интернетом")
                .setPositiveButton(getString(R.string.error_window_btn)) { _, _ -> requireActivity().finish() }
                .show()
        }
    }

    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {

            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreference = SharedPreference(requireContext())
        Handler().postDelayed({
            if (!checkForInternet(requireContext())) {
                showErrorWindow()
            } else {
                if (sharedPreference.isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                }
                when {
                    sharedPreference.isFirstRun -> {
                        findNavController().navigate(R.id.action_splashFragment_to_introFragment)
                    }
                    sharedPreference.token != "" -> {
                        findNavController().navigate(R.id.action_splashFragment_to_taskFragment)
                    }
                    else -> {
                        findNavController().navigate(R.id.action_splashFragment_to_registrationFragment)
                    }
                }
            }
        }, 2000)
    }
}