package com.dxshulya.wasabi.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_introFragment)
        }, 2000)
    }
}