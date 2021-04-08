package com.ifyezedev.coslog

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.ifyezedev.coslog.databinding.ActivityCosplayBinding

class CosplayActivityCompositionRoot(private val activity: AppCompatActivity) {
    val cosplayController: NavController
        get() {
            val navHostFragment =
                activity.supportFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
            return navHostFragment.navController
        }

    val appBar: MaterialToolbar = activity.findViewById(R.id.topAppBar)
}