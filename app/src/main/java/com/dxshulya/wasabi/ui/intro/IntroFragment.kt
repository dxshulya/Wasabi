package com.dxshulya.wasabi.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dxshulya.wasabi.R
import com.dxshulya.wasabi.databinding.FragmentIntroBinding
import com.dxshulya.wasabi.ui.intro.screens.FirstIntroFragment
import com.dxshulya.wasabi.ui.intro.screens.SecondIntroFragment
import com.dxshulya.wasabi.ui.intro.screens.ThirdIntroFragment

class IntroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentIntroBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf(
            FirstIntroFragment(),
            SecondIntroFragment(),
            ThirdIntroFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        return binding.root
    }
}