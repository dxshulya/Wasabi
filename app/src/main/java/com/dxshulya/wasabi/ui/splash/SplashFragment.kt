package com.dxshulya.wasabi.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.datastore.SharedPreference

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreference = SharedPreference(requireContext())
        Handler().postDelayed({
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
        }, 2000)
    }
}