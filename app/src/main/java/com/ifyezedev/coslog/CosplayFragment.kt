package com.ifyezedev.coslog

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.databinding.FragmentCosplayMainBinding

class CosplayFragment : CosplayBaseFragment<FragmentCosplayMainBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_cosplay_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding {
            appBarInclude.topAppBar.setNavigationOnClickListener {
                dialogsController.navigate(R.id.homeActivity)
            }
        }
    }

    override fun onStart() {
        binding.bottomNav.setupWithNavController(dialogsController)
        super.onStart()
    }
}