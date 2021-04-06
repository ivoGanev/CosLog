package com.ifyezedev.coslog

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class CosplayActivityCompositionRoot(private val activity: AppCompatActivity) {
    lateinit var cosplayController: NavController

    fun createCosplayController(fragment: Fragment): NavController {
        val navHostFragment =
            fragment.childFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        cosplayController = navHostFragment.navController
        return cosplayController
    }
}