package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar

class CosplayActivityCompositionRoot(private val activity: AppCompatActivity) {
    val cosplayController: NavController
        get() {
            val navHostFragment =
                activity.supportFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
            return navHostFragment.navController
        }

    val appBar: MaterialToolbar = activity.findViewById(R.id.toolbar)

    val cosplayToolbarController: CosplayToolbarController by lazy {
        CosplayToolbarController(activity.findViewById(R.id.viewFlipper))
    }
}