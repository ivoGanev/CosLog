package com.ifyezedev.coslog

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.util.Log.e
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ifyezedev.coslog.databinding.FragmentCosplayMainBinding

class CosplayFragmentMain : BindingFragment<FragmentCosplayMainBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_cosplay_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //enable the Up button
        binding.appBarInclude.topAppBar.setNavigationOnClickListener {
            val nav = Navigation.findNavController(binding.cosplayNavHostFragment)
            nav.navigate(R.id.homeActivity)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val nav = Navigation.findNavController(binding.cosplayNavHostFragment)
        binding.bottomNav.setupWithNavController(nav)
    }
}