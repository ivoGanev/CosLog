package com.ifyezedev.coslog

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.databinding.FragmentCosplayMainBinding

class CosplayGraphFragment : CosplayGraphBaseFragment<FragmentCosplayMainBinding>() {
    override fun bindingLayoutId(): Int = R.layout.fragment_cosplay_main

    private lateinit var dialogsController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostController = childFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        dialogsController = navHostController.navController

        binding.appBarInclude.topAppBar.setNavigationOnClickListener {
            dialogsController.navigate(R.id.homeActivity)
        }
        binding.bottomNav.setupWithNavController(dialogsController)
    }
}