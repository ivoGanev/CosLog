package com.ifyezedev.coslog.core.di.activity

import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ifyezedev.coslog.R
import dagger.Module
import dagger.Provides

@Module
class CosplayActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @CosplayActivityScope
    fun cosplayController(): NavController {
        val navHostFragment =
            activity.supportFragmentManager.findFragmentById(R.id.cosplayNavHostFragment) as NavHostFragment
        return navHostFragment.navController
    }

    @Provides
    fun actionBar() : ActionBar = activity.supportActionBar!!
}