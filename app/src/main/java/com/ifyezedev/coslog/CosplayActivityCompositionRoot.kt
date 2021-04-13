package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class CosplayActivityCompositionRoot(private val activity: AppCompatActivity) {
    val cosplayController: NavController
        get() {
            val navHostFragment =
                activity.supportFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
            return navHostFragment.navController
        }

    val appBar = activity.findViewById<Toolbar>(R.id.appToolbar)
}