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

class CosplayCompositionRoot(private val activity: AppCompatActivity) {
    private val fragmentManager get() = activity.supportFragmentManager
    val cosplayController: NavController
        get() {
            val navHostFragment =
                fragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
            return navHostFragment.navController
        }

    fun dialogsController(fragment: Fragment): NavController {
        val navHostFragment = fragment.childFragmentManager
            .findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        return navHostFragment.navController
    }
}