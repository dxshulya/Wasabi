package com.dxshulya.wasabi.ui.intro.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.FragmentThirdIntroBinding
import com.dxshulya.wasabi.domain.datastore.SharedPreference

class ThirdIntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentThirdIntroBinding.inflate(inflater, container, false)

        val sharedPreference = SharedPreference(requireContext())

        binding.registrationButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_introFragment_to_registrationFragment)
            sharedPreference.isFirstRun = false

        }
        binding.loginButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_introFragment_to_loginFragment)
            sharedPreference.isFirstRun = false
        }

        return binding.root
    }
}