package com.ifyezedev.coslog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ifyezedev.coslog.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialize binding variable
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)

        //find NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        //initialize drawerLayout variable
        drawerLayout = binding.drawerLayout

        //link navcontroller to ActionBar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //connect drawerLayout to navigation graph
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        //hook up the navigation UI up to the navigation view
        binding.navView.setupWithNavController(navController)

        //use the go button to navigate to CosplayActivity
        binding.goBttn.setOnClickListener {
            goToCosplayActivity()
        }
    }

    private fun goToCosplayActivity() {
        navController.navigate(R.id.action_cosplayFragment_to_cosplayActivity)
    }

    //let activity handle Up navigation
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}